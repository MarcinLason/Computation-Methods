package Lab2;

import java.util.Random;

import org.apache.commons.math3.linear.*;

public class LUFactorial {
	
	final static int SIZE = 3000;
	static double[][] Matrix = new double[SIZE][SIZE];
	static double[][] L = new double[SIZE][SIZE];
	static double[][] U = new double[SIZE][SIZE];
	
	
	public static void fillMatrix(double[][] X) {
		Random rand = new Random();
		for(int i = 0; i < SIZE; i++) {
			for(int j = 0; j < SIZE; j++) {
				X[i][j] = (rand.nextDouble() * (rand.nextInt(1000) - 500 ));
			}
		}
		
	}
	
	public static void draw(double[][] Matrix) {
		for(int i = 0; i < SIZE; i++) {
			for(int j = 0; j < SIZE - 1; j++ ) {
				System.out.print(Matrix[i][j] + " ");
			}
			System.out.println(Matrix[i][SIZE-1]);
		}
		System.out.println("");
	}

	public static void factorise() {
		for(int k = 0; k < SIZE; k++){
			L[k][k] = 1;
			
			for(int j = k; j < SIZE; j++) {
				double sigma = 0;
				for(int s = 0; s < k; s++) {
					sigma = sigma + L[k][s] * U[s][j];
				}
				U[k][j] = Matrix[k][j] - sigma;
			}
			
			for(int i = k + 1; i < SIZE; i++) {
				double sigma = 0;
				for(int s = 0; s < k; s++) {
					sigma = sigma + L[i][s] * U[s][k];
				}
				L[i][k] = (Matrix[i][k] - sigma) / U[k][k];
			}
		}
		
	}
	
	public static void main(String[] args) {
		fillMatrix(Matrix);
		RealMatrix A = new Array2DRowRealMatrix(Matrix); //Matrix is copied, it is not delivering reference 
		
		
		//draw(Matrix);
		long startTime1 = System.nanoTime();
		factorise();
		long estimatedTime1 = System.nanoTime() - startTime1;
		System.out.println("My version has been working for " + estimatedTime1 + " nanoseconds.");
		
		
		//draw(Matrix);
		//draw(L);
		//draw(U);
		
		long startTime2 = System.nanoTime();
		LUDecomposition ALU = new LUDecomposition(A);
		long estimatedTime2 = System.nanoTime() - startTime2;
		System.out.println("Library's version has been working for " + estimatedTime2 + " nanoseconds.");
		
		
		//Checking the output
		//draw(Matrix);
		//draw(ALU.getL().getData());
		//draw(ALU.getU().getData());
		
	}

}
