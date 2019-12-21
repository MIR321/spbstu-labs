package ru.spbstu.telematics.lab1;
import org.junit.Test;
import org.junit.Assert;

public class MatrixMultiplicationTest {
	
	@Test
    public void testEquality() {
    	double[][] A = {{1,2,3},{4,5,6}};
		double[][] B = {{2,3,4},{4,5,6}};
        Assert.assertFalse(MatrixMultiplication.isEqual(A, B));
        B = new double[][]{{1,2,3},{4,5,6}};
        Assert.assertTrue(MatrixMultiplication.isEqual(A, B));
    }
	
    @Test
    public void testMultiplication() {
    	
    	double[][] A = {{-1.345, 4.33, 6.2}, {1.5, -34, 3.2}, {6.7, 5.3, -3.3}};
		double[][] B = {{2.3, 6.7}, {5.6, -2.55}, {8.8, 1.23}};
		double[][] C = MatrixMultiplication.multiply(A,B);
		double[][] result = {{75.7145, -12.427}, {-158.79, 100.686}, {16.05, 27.316}};
        Assert.assertTrue(MatrixMultiplication.isEqual(result, C));
        
        A = new double[][]{{2, 3, 5, 6, 7.2, -5.22, 4}};
        B = new double[][]{{1}, {2}, {-4.33}, {2}, {3}, {7.7}, {2}};
        C = MatrixMultiplication.multiply(A,B);
        result = new double[][]{{-12.244}};
        Assert.assertTrue(MatrixMultiplication.isEqual(C, result));
        
        A = null;
        B = null;
        result = null;
        C = MatrixMultiplication.multiply(A,B);
        Assert.assertTrue(MatrixMultiplication.isEqual(C, result));
        
        A = new double[][] {{1,2},{1,2}};
        B = new double[][] {{1,2},{1,2}};
        C = MatrixMultiplication.multiply(A,B);
        result = new double[][]{{3, 5}, {3, 6}};
        Assert.assertFalse(MatrixMultiplication.isEqual(C, result));
        result = new double[][]{{3, 6}, {3, 6}};
        Assert.assertTrue(MatrixMultiplication.isEqual(C, result));
        
        A = new double[][]{{-1.345, 4.33, 6.2,5}, {1.5, -34, 3.2}, {6.7, 5.3, -3.3}};
		B = new double[][]{{2.3, 6.7}, {5.6, -2.55}, {8.8, 1.23}};
		C = MatrixMultiplication.multiply(A,B);
		result = null;
        Assert.assertTrue(MatrixMultiplication.isEqual(result, C));
        
    }

}
