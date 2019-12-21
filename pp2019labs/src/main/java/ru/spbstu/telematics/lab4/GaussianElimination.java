package ru.spbstu.telematics.lab4;
import java.text.DecimalFormat;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.*;

public class GaussianElimination {

    public static double[][] matrix;
    private static double[][] matrixCopy;
    public static int numberOfRows = 3;
    public static int numberOfColumns = 3;
    private static final double EPS = 1e-8;
    public static int numberOfThreads = 4;
    public static Boolean slowBackSubstitution = true;
    private ExecutorService executorService;
    public static double[] answer = new double[numberOfColumns -1];
    public  Boolean someRoots = false;
    public  Boolean infSolutions = false;
    public  Boolean correct = false;

    /**
     * Копирует матрицу matrix для проверки решения
     * @param matrix
     * @return
     */
    private double[][] deepCopy(double[][] matrix) {
        return java.util.Arrays.stream(matrix).map(el -> el.clone()).toArray($ -> matrix.clone());
    }

    /**
     * Инициализирует матрицу matrix
     * @throws FileNotFoundException 
     */
    private void initialize(String filename){
    	
    	if(filename == null) {
	        matrix = new double [numberOfRows][numberOfColumns];
	        for (int i = 0; i < numberOfRows; i++)
	            for (int j = 0; j < numberOfColumns; j++)
	                matrix[i][j] = 100*Math.random();
	        answer = new double[numberOfColumns -1];
    	}else {
    		File file = new File(filename);
            try{
            	Scanner scan = new Scanner(file);
            	numberOfRows = scan.nextInt();
            	numberOfColumns = scan.nextInt();
            	matrix = new double [numberOfRows][numberOfColumns];
            	answer = new double[numberOfColumns -1];
                for (int i = 0; i < numberOfRows; i++) {
                    for(int j = 0; j < numberOfColumns && scan.hasNextDouble(); j++){
                        matrix[i][j] = scan.nextDouble();
                    }
                }
                scan.close();
            }catch(FileNotFoundException e){
            	System.out.println("FILE NOT FOUND!");
            	return;
            }
    	}


        matrixCopy = deepCopy(matrix);
        System.out.println("Init finished");
    }

    /**
     * Решает СЛАУ методом Гаусса
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public void eliminate(String filename) throws ExecutionException, InterruptedException {
    	executorService = Executors.newFixedThreadPool(numberOfThreads);
    	System.out.println("Number of Threads: " + numberOfThreads);
        initialize(filename);
        forward();
        //printMatrix(matrix);
        if(checkSolutions() == 2){
            System.out.println("There are infinitely many solutions");
            infSolutions = true;
        }else if (checkSolutions() == 1){
            System.out.println("There are no roots");
            someRoots = false;
        }else {
            someRoots = back();
            if(someRoots) {
                check();
            }else{
                System.out.println("There are no roots");
            }
        }
        executorService.shutdown();
        System.out.println("FINISHED");
        //elimination.printMatrix(elimination.matrix);
        if(answer.length < 40) {
	        for(int i = 0; i < answer.length; i++) {
	        	System.out.println("x" + i + " = " + answer[i]);
	        }
        }
    }

    /**
     * Проверяет наличие, отсутствие корней
     * @return 0 = СЛАУ возможно совместна, 1 = СЛАУ несовместна, 2 = СЛАУ имеет не единственное решение
     */
    private int checkSolutions(){

        for(int j = numberOfRows -1; j >= 0; j--) {
            for (int i = numberOfColumns - 2; i >= 0; i--)
                if (Math.abs(matrix[j][i]) > EPS && numberOfColumns-1 > numberOfRows) {
                    return 2; //There are infinitely many solutions
                }else if(Math.abs(matrix[j][i]) > EPS){
                    return 0; //Threre are several roots
                }

            if(Math.abs(matrix[j][numberOfColumns -1]) > EPS)
                return 1; //Threre are no roots
        }

        return 0;
    }

    /**
     * Осуществляет обратный ход метода Гаусса
     * Если slowBackSubstitution = true, выполняется "быстрый" последовательный алгоритм
     * @return true - есть единственное решение, false - решений нет
     * @throws InterruptedException
     * @throws ExecutionException
     */
    private Boolean back() throws InterruptedException, ExecutionException {
        long time = System.currentTimeMillis();
        if(!slowBackSubstitution || numberOfColumns - numberOfRows != 1) {
            for (int i = Math.min(numberOfColumns - 2, numberOfRows - 1); i >= 0; i--) {
                double sum = 0.0;
                for (int j = i + 1; j < numberOfColumns - 1; j++) {
                    sum += matrix[i][j] * answer[j];
                }
                if (Math.abs(matrix[i][i]) > EPS) {
                    answer[i] = (matrix[i][numberOfColumns - 1] - sum) / matrix[i][i];
                }else if (Math.abs(matrix[i][numberOfColumns -1] - sum) > EPS) {
                    return false;
                }
            }
        }
        else if(numberOfThreads == 1) {
            for (int i = 0; i < numberOfRows; i++) {
                answer[i] = matrix[i][numberOfColumns - 1];
            }
            for (int j = numberOfRows - 1; j >= 0; j--) {
                if (Math.abs(matrix[j][j]) > EPS) {
                    answer[j] = answer[j]/matrix[j][j];
                }else if (Math.abs(answer[j]) > EPS) {
                    return false;
                }
                for (int i = 0; i <= j - 1; i++) {
                    answer[i] -= matrix[i][j] * answer[j];
                }
            }
        }else{
            for(int i = 0; i < numberOfRows; i++){
                answer[i] = matrix[i][numberOfColumns -1];
            }
            for(int j = numberOfRows - 1; j >= 0; j--){
                //System.out.println("j = " + j);
                if (Math.abs(matrix[j][j]) > EPS) {
                    answer[j] = answer[j]/matrix[j][j];
                }else if (Math.abs(answer[j]) > EPS) {
                    return false;
                }
                if(j == 0)
                    break;
                List<Substitution> taskList = new ArrayList<Substitution>(numberOfThreads);
                int step = (j-1)/numberOfThreads;
                if(step == 0)
                    step = 1;
                for(int i = 0; i < numberOfThreads; i++){
                    int first = i*step;
                    int second = (i+1)*step-1;
                    if(i == numberOfThreads-1 && second != j-1)
                        second = j == 0 ? 0 : j-1;
                    taskList.add(new Substitution(first, second, j));
                    if(first >= j-1)
                        break;
                }
                List<Future<Integer>> results = executorService.invokeAll(taskList);
                for (Future<Integer> f : results) {
                    f.get();
                }
            }

        }
        System.out.println("back substitution: " + (System.currentTimeMillis() - time)/1000.0 + " seconds");
        return true;
    }

    /**
     * Осуществляет прямой ход метода Гаусса
     * @throws InterruptedException
     * @throws ExecutionException
     */
    private void forward() throws InterruptedException, ExecutionException {

        long time = System.currentTimeMillis();

        for (int p = 0; p < Math.min(numberOfRows, numberOfColumns -1); p++){

            int max = p;
            for (int i = p + 1; i < numberOfRows; i++){
                if (Math.abs(matrix[i][p]) > Math.abs(matrix[max][p])){
                    max = i;
                }
            }

            swapRows(p, max);

            if (Math.abs(matrix[p][p]) <= EPS) {
                continue;
            }

            if(numberOfThreads != 1) {
                int step = (numberOfRows - 1 - p) / numberOfThreads;
                if (step == 0)
                    step = 1;
                List<Subtraction> taskList = new ArrayList<Subtraction>();
                for (int i = 0; i < numberOfThreads; i++) {
                    int first = (p + 1) + i * step;
                    int second = (p + 1) + (i + 1) * step;
                    if (second > numberOfRows)
                        second = numberOfRows;
                    if (first > numberOfRows)
                        break;
                    if(i == numberOfThreads-1 && second < numberOfRows)
                        second = numberOfRows;
                    taskList.add(new Subtraction(p, first, second));
                }

                List<Future<Integer>> results = executorService.invokeAll(taskList);
                for (Future<Integer> f : results) {
                    f.get();
                }
            }
            else{
                subtract(p);
            }
        }
        System.out.println("forward elimination: " + (System.currentTimeMillis() - time)/1000.0 + " seconds");
    }

    /**
     * Меняет местами две строки матрицы matrix
     * @param row1
     * @param row2
     */
    private void swapRows(int row1, int row2) {
        double[] temp = matrix[row1];
        matrix[row1] = matrix[row2];
        matrix[row2] = temp;
    }

    /**
     * Используется в последовательном варианте прямого хода метода Гаусса
     * @param p
     */
    private void subtract(int p){
        for (int i = p + 1; i < numberOfRows; i++){
            double k = matrix[i][p] / matrix[p][p];
            for (int j = p; j < numberOfColumns; j++){
                matrix[i][j] -= k * matrix[p][j];
                if(Math.abs(matrix[i][j]) < EPS)
                    matrix[i][j] = 0;
            }
        }
    }
    
    /**
     * Печать матрицы matrix
     * @param matrix
     */
    public void printMatrix(double[][] matrix){
        for(int i = 0; i < matrix.length; i++){
            for(int j = 0; j < matrix[0].length; j++){
                System.out.print(new DecimalFormat("##.##").format(matrix[i][j])+ " ");
            }
            System.out.println();
        }
    }

    /**
     * Проверяет найденное решение СЛАУ путем подстановки в исходную матрицу
     */
    private void check() {

        System.out.println();
        double b = 0;
        for(int i = 0; i < matrixCopy.length; i++){
            b = 0;
            for(int j = 0; j < answer.length; j++) {
                b += answer[j] * matrixCopy[i][j];
            }
            if(Math.abs(b-matrixCopy[i][numberOfColumns -1]) > 0.0001){
                System.out.println("WRONG ANSWER");
                correct = false;
                return;
            }
        }
        System.out.println("CORRECT ANSWER");
        correct = true;
    }

//    public static void main(String[] args) throws InterruptedException, ExecutionException, IOException {
//
//        System.in.read();
//        GaussianElimination elimination = new GaussianElimination();
//        elimination.eliminate("./matrixes/5x2");
//    }

    /**
     * Асинхронное вычисление для прямого хода метода Гаусса
     */
    static class Subtraction implements Callable<Integer> {
        int p;
        int first;
        int second;
        public Subtraction(int p, int first, int second){
            this.p = p;
            this.first = first;
            this.second = second;
        }
        @Override
        public Integer  call() throws Exception {
            for (int i = first; i < second; i++){
                double k = matrix[i][p] / matrix[p][p];
                for (int j = p; j < numberOfColumns; j++){
                    matrix[i][j] -= k * matrix[p][j];
                    if(Math.abs(matrix[i][j]) < EPS)
                        matrix[i][j] = 0;
                }

            }
            return 1;
        }
    }
    
    /**
     * Асинхронное вычисление для обратного хода метода Гаусса (медленный алгоритм)
     */
    static class Substitution implements Callable<Integer> {
        int j;
        int first;
        int second;
        public Substitution(int first, int second, int j){
            this.first = first;
            this.second = second;
            this.j = j;
        }
        @Override
        public Integer  call() throws Exception {
            for(int i = first; i <= second; i++){
                answer[i] -= matrix[i][j]*answer[j];
            }
            return 1;
        }
    }

}

