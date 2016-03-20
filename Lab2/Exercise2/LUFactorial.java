package Lab2;

import java.util.Random;

import org.apache.commons.math3.linear.*;

public class LUFactorial {
	//Function check works only without pivoting. It is caused by the fact that pivoting changes configuration of verses.
	final static int SIZE = 1000;
	static double[][] OriginalMatrix = new double[SIZE][SIZE];
	static double[][] Matrix = new double[SIZE][SIZE];
	static double[][] CheckMatrix = new double[SIZE][SIZE];
	static double[][] L = new double[SIZE][SIZE];
	
	public static void prepareMatrixes() {
		Random rand = new Random();
		for(int i = 0; i < SIZE; i++) {
			for(int j = 0; j < SIZE; j++) {
				Matrix[i][j] = (rand.nextDouble() * (rand.nextInt(1000) - 500 ));
			}
		}
		for(int i = 0; i < SIZE; i++) { //Writing 1 on the diagonal of L matrix
			L[i][i] = 1;
		}
		
		for(int i = 0; i < SIZE; i++) {
			for(int j = 0; j < SIZE; j++) {
				OriginalMatrix[i][j] = Matrix[i][j];
			}
		}
	}
	
	public static Point findPivot(int startIndex) { //partial Pivoting
		Point result = new Point(startIndex,startIndex, Math.abs(Matrix[startIndex][startIndex]));
		
		for(int i = startIndex; i < SIZE; i++) {
				if(Math.abs(Matrix[i][startIndex]) > result.value){
					result.value = Math.abs(Matrix[i][startIndex]);
					result.x = i;
					result.y = startIndex;
				}
			}		
		return result;
	}
	
	public static void draw () { //function created to test algorithm
		System.out.println("MATRIX:");
		
		for(int i = 0; i < SIZE; i++) {
			for(int j = 0; j < SIZE - 1; j++) {
				System.out.print(Matrix[i][j] + "(-------------)");
			}
			System.out.println(Matrix[i][SIZE - 1]);
		}
		
		System.out.println("L:");
		for(int i = 0; i < SIZE; i++) {
			for(int j = 0; j < SIZE - 1; j++) {
				System.out.print(L[i][j] + "(-------------)");
			}
			System.out.println(L[i][SIZE - 1]);
		}
	}
	
	
	
	public static void swapPivot(int startIndex) {
		Point pivot = findPivot(startIndex); //finding Pivot
		
		if(startIndex == pivot.x) { //if Pivot was found on the right place
			return;
		}
		
		if(startIndex != pivot.x) { //changing verses 
			//swaping all values from the Matrix between startIndex's verse and pivot's verse
			double c;
			for(int i = 0; i < SIZE; i++) {
				c = Matrix[startIndex][i];
				Matrix[startIndex][i] = Matrix[pivot.x][i];
				Matrix[pivot.x][i] = c;
			}
			pivot.x = startIndex;		
		}
		
	}
	
	public static void factorize() { //function using Gauss algorithm to 
		int startIndex = 0;
		while(startIndex < SIZE){
			swapPivot(startIndex);
			double ratio;
		
			for(int i = startIndex + 1; i < SIZE; i++) {
				ratio = ((Matrix[i][startIndex]) / (Matrix[startIndex][startIndex]));
				L[i][startIndex] = ratio;
			
				for(int j = startIndex; j < SIZE; j++) {
					Matrix[i][j] = Matrix[i][j] - ratio * Matrix[startIndex][j];
				}
			}	
		startIndex++;
		}
	}
	
	public static boolean check() {
		for(int i = 0; i < SIZE; i++) {
			for(int j = 0; j < SIZE; j++) {
				double result = 0.0;
				
				for(int s = 0; s < SIZE; s++) {
					result = result + (L[i][s] * Matrix[s][j]);
				}
				CheckMatrix[i][j] = result;			
			}
		}
		
		
		boolean isTheSame = true;
		for(int i = 0; i < SIZE; i++) {
			for(int j = 0; j < SIZE; j++) {
				if(Math.abs(OriginalMatrix[i][j] - CheckMatrix[i][j]) > 0.000001) {
					isTheSame = false;
				}
			}
		}
		return isTheSame;
	}
	public static void main(String[] args) {
		prepareMatrixes();
		factorize();
		//boolean check = check();
		//System.out.println("Is well factorized?" + check);
	}
}


