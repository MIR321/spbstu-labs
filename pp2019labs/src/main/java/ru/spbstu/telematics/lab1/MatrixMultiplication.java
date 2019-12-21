package ru.spbstu.telematics.lab1;


public class MatrixMultiplication {
	
	public static double[][] multiply(double[][] A, double[][] B){

		if(A == null || B == null) {
			System.out.println("Error");
			return null;
		}
		
		boolean err = false;
		int nA = A.length;
		int mA = A[0].length;
		int nB = B.length;
		int mB = B[0].length;
			
		if(nA == 0 || mA == 0 ||  nB == 0 || mB == 0 || mA != nB) 
			err = true;
			
		for(int i = 1; i < nA; i++) 
			if(A[i].length != A[i-1].length) 
				err = true;
					
			
		for(int i = 1; i < nB; i++) 
			if(B[i].length != B[i-1].length) 
				err = true;
		
		if(err) {
			System.out.println("Input Error");
			return null;
		}
					
		double[][] result = new double[nA][mB];
		
		for(int i = 0; i < nA; i++) {
			for(int j = 0; j < mB; j++) {
				double sum = 0;
				for(int k = 0; k < mA; k++) {
					sum += A[i][k]*B[k][j];
				}
				result[i][j] = sum;
			}
		}
		
		
		return result;
	}
	
	public static boolean isEqual(double[][] A, double[][] B) {
		if(A == null && B == null)
			return true;
		else if(A == null || B == null || A.length != B.length)
			return false;
		
		for(int i = 0; i < A.length; i++) {
			if(A[i].length != B[i].length)
				return false;
			for(int j = 0; j < A[i].length; j++) {
				if(Math.abs(A[i][j] - B[i][j]) > 1E-5)
					return false;
			}
		}
		
		return true;
	}

	public static void main(String[] args){
		
		double[][] A = null;
		double[][] B = null;
		double[][] C = multiply(A,B);
		double[][] result = {{75.7145, -12.427}, {-158.79, 100.686}, {16.05, 27.316}};
		System.out.print(isEqual(result,C));
	}

}
