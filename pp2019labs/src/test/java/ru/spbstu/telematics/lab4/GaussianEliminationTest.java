package ru.spbstu.telematics.lab4;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import org.junit.Assert;
import org.junit.Test;


public class GaussianEliminationTest {
	
	public Boolean compareAnswers(double[] myAnswer, double[] answer) {
		if(myAnswer.length != answer.length)
			return false;
	
		for(int i = 0; i < answer.length; i++) {
			if(Math.abs(answer[i]-myAnswer[i]) > 1e-8)
				return false;
		}
		
		return true;
	}

	@Test
	public void test3x3Matrix() throws ExecutionException, InterruptedException {
		{
			System.out.println("test3x3Matrix-------------------------------------- 1 THREAD");
			GaussianElimination.numberOfThreads = 1;
			GaussianElimination elimination = new GaussianElimination();
			elimination.eliminate("./matrixes/matrix1.txt");
			double[] answer = {-4.0, -13.0, 11.0};
			Assert.assertTrue(compareAnswers(answer, elimination.answer));
			answer = new double[]{-4.0, -13.0, 11.0001};
			Assert.assertFalse(compareAnswers(answer, elimination.answer));
			elimination.printMatrix(elimination.matrix);
			System.out.println("---------------------------------------------------");
		}
		{
			System.out.println("test3x3Matrix-------------------------------------- 4 THREADS");
			GaussianElimination.numberOfThreads = 4;
			GaussianElimination elimination = new GaussianElimination();
			elimination.eliminate("./matrixes/matrix1.txt");
			double[] answer = {-4.0, -13.0, 11.0};
			Assert.assertTrue(compareAnswers(answer, elimination.answer));
			answer = new double[]{-4.0, -13.0, 11.0001};
			Assert.assertFalse(compareAnswers(answer, elimination.answer));
			Assert.assertTrue(elimination.someRoots);
			Assert.assertFalse(elimination.infSolutions);
			elimination.printMatrix(elimination.matrix);
			System.out.println("---------------------------------------------------");
		}
	}
	
	@Test
	public void test3x4MatrixNoSolution() throws ExecutionException, InterruptedException {
		{
			System.out.println("test3x4MatrixNoSolution-------------------------------------- 1 THREAD");
			GaussianElimination.numberOfThreads = 1;
			GaussianElimination elimination = new GaussianElimination();
			elimination.eliminate("./matrixes/3x4NoSolution");
			elimination.printMatrix(elimination.matrix);
			Assert.assertFalse(elimination.someRoots);
			System.out.println("---------------------------------------------------");
		}
		{
			System.out.println("test3x4MatrixNoSolution-------------------------------------- 10 THREADS");
			GaussianElimination.numberOfThreads = 10;
			GaussianElimination elimination = new GaussianElimination();
			elimination.eliminate("./matrixes/3x4NoSolution");
			elimination.printMatrix(elimination.matrix);
			Assert.assertFalse(elimination.someRoots);
			System.out.println("---------------------------------------------------");
		}
	}
	
	@Test
	public void test3x2Matrix() throws ExecutionException, InterruptedException {
		{
			System.out.println("test3x2Matrix-------------------------------------- 4 THREADS");
			GaussianElimination.numberOfThreads = 4;
			GaussianElimination elimination = new GaussianElimination();
			elimination.eliminate("./matrixes/3x2");
			double[] answer = {1.0,2.0};
			Assert.assertTrue(compareAnswers(answer, elimination.answer));
			answer = new double[]{1.001,2};
			Assert.assertFalse(compareAnswers(answer, elimination.answer));
			Assert.assertTrue(elimination.someRoots);
			elimination.printMatrix(elimination.matrix);
			System.out.println("---------------------------------------------------");
		}
	}
	
	@Test
	public void test3x5Matrix() throws ExecutionException, InterruptedException {
		{
			System.out.println("test3x5Matrix-------------------------------------- 1 THREADS");
			GaussianElimination.numberOfThreads = 1;
			GaussianElimination elimination = new GaussianElimination();
			elimination.eliminate("./matrixes/3x5Inf");
			double[] answer = {1.0,2.0,3,4,5};
			Assert.assertFalse(compareAnswers(answer, elimination.answer));
			Assert.assertTrue(elimination.infSolutions);
			elimination.printMatrix(elimination.matrix);
			System.out.println("---------------------------------------------------");
		}
	}
	
	@Test
	public void test5x2Matrix() throws ExecutionException, InterruptedException {
		{
			System.out.println("test5x2Matrix-------------------------------------- 1 THREADS");
			GaussianElimination.numberOfThreads = 1;
			GaussianElimination elimination = new GaussianElimination();
			elimination.eliminate("./matrixes/5x2");
			elimination.printMatrix(elimination.matrix);
			double[] answer = {1.0, 0.5};
			Assert.assertTrue(compareAnswers(answer, elimination.answer));
			Assert.assertFalse(elimination.infSolutions);
			Assert.assertTrue(elimination.someRoots);
			elimination.printMatrix(elimination.matrix);
			System.out.println("---------------------------------------------------");
		}
		{
			System.out.println("test5x2Matrix-------------------------------------- 8 THREADS");
			GaussianElimination.numberOfThreads = 8;
			GaussianElimination elimination = new GaussianElimination();
			elimination.eliminate("./matrixes/5x2");
			double[] answer = {1, 0.5};
			Assert.assertTrue(compareAnswers(answer, elimination.answer));
			Assert.assertFalse(elimination.infSolutions);
			Assert.assertTrue(elimination.someRoots);
			elimination.printMatrix(elimination.matrix);
			System.out.println("---------------------------------------------------");
		}
	}
	
	@Test
	public void test4x4Matrix() throws ExecutionException, InterruptedException {
		{
			System.out.println("test4x4Matrix-------------------------------------- 5 THREADS");
			GaussianElimination.numberOfThreads = 5;
			GaussianElimination elimination = new GaussianElimination();
			elimination.eliminate("./matrixes/4x4");
			elimination.printMatrix(elimination.matrix);
			double[] answer = {-3, -1, 2, 7};
			Assert.assertTrue(compareAnswers(answer, elimination.answer));
			Assert.assertFalse(elimination.infSolutions);
			Assert.assertTrue(elimination.someRoots);
			elimination.printMatrix(elimination.matrix);
			System.out.println("---------------------------------------------------");
		}
		{
			System.out.println("test4x4Matrix-------------------------------------- 1 THREADS");
			GaussianElimination.numberOfThreads = 1;
			GaussianElimination.slowBackSubstitution = false;
			GaussianElimination elimination = new GaussianElimination();
			elimination.eliminate("./matrixes/4x4");
			elimination.printMatrix(elimination.matrix);
			double[] answer = {-3, -1, 2, 7};
			Assert.assertTrue(compareAnswers(answer, elimination.answer));
			Assert.assertFalse(elimination.infSolutions);
			Assert.assertTrue(elimination.someRoots);
			elimination.printMatrix(elimination.matrix);
			System.out.println("---------------------------------------------------");
		}
	}
	
	@Test
	public void testRandom3000x3000Matrix() throws ExecutionException, InterruptedException {

		{
			System.out.println("test3000x3000Matrix-------------------------------------- 4 THREADS, SLOW BACK");
			GaussianElimination.numberOfThreads = 4;
			GaussianElimination.numberOfColumns = 3001;
			GaussianElimination.numberOfRows = 3000;
			GaussianElimination.slowBackSubstitution = true;
			GaussianElimination elimination = new GaussianElimination();
			elimination.eliminate(null);
			Assert.assertFalse(elimination.infSolutions);
			Assert.assertTrue(elimination.someRoots);
			Assert.assertTrue(elimination.correct);
			System.out.println("---------------------------------------------------");
		}
		{
			System.out.println("test3000x3000Matrix-------------------------------------- 1 THREAD, SLOW BACK");
			GaussianElimination.numberOfThreads = 1;
			GaussianElimination.numberOfColumns = 3001;
			GaussianElimination.numberOfRows = 3000;
			GaussianElimination.slowBackSubstitution = true;
			GaussianElimination elimination = new GaussianElimination();
			elimination.eliminate(null);
			Assert.assertFalse(elimination.infSolutions);
			Assert.assertTrue(elimination.someRoots);
			Assert.assertTrue(elimination.correct);
			System.out.println("---------------------------------------------------");
		}
		{
			System.out.println("test3000x3000Matrix-------------------------------------- 4 THREADS, FAST BACK");
			GaussianElimination.numberOfThreads = 4;
			GaussianElimination.numberOfColumns = 3001;
			GaussianElimination.numberOfRows = 3000;
			GaussianElimination.slowBackSubstitution = false;
			GaussianElimination elimination = new GaussianElimination();
			elimination.eliminate(null);
			Assert.assertFalse(elimination.infSolutions);
			Assert.assertTrue(elimination.someRoots);
			Assert.assertTrue(elimination.correct);
			System.out.println("---------------------------------------------------");
		}
		{
			System.out.println("test3000x3000Matrix-------------------------------------- 1 THREAD, FAST BACK");
			GaussianElimination.numberOfThreads = 1;
			GaussianElimination.numberOfColumns = 3001;
			GaussianElimination.numberOfRows = 3000;
			GaussianElimination.slowBackSubstitution = false;
			GaussianElimination elimination = new GaussianElimination();
			elimination.eliminate(null);
			Assert.assertFalse(elimination.infSolutions);
			Assert.assertTrue(elimination.someRoots);
			Assert.assertTrue(elimination.correct);
			System.out.println("---------------------------------------------------");
		}
	}
	
}
