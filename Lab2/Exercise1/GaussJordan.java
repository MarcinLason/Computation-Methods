package Lab2;

import java.util.Random;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;


public class GaussJordan {
	final static int SIZE = 400;
	static double[][] Matrix = new double[SIZE][SIZE];
	static double[] FreeTerms = new double[SIZE];
	static double[] Unknowns = new double[SIZE];
	static double[][] LibMatrix = new double[SIZE][SIZE + 1];
	
	public static void fillMatrix() {
		Random rand = new Random();
		for(int i = 0; i < SIZE; i++) {
			for(int j = 0; j < SIZE; j++) {
				Matrix[i][j] = (rand.nextDouble() * (rand.nextInt(1000) - 500 ));
				LibMatrix[i][j] = Matrix[i][j];
			}
		}
		
	}
	
	public static void fillFreeTerms() {
		Random rand = new Random();
		for(int i = 0; i < SIZE; i++){
			FreeTerms[i] = rand.nextDouble() * (rand.nextInt(1000) - 500 );
			LibMatrix[i][SIZE] = FreeTerms[i];
		}
	}
	
	public static void fillUnknows() {
		for(int i = 0; i < SIZE; i++) {
			Unknowns[i] = i;
		}
	}
	
	public static void prepareMatrixes() {
		fillMatrix();
		fillFreeTerms();
		fillUnknows();
	}
	
	public static void swapPivot(int startIndex) { //method which set Pivot in the right place to start algorithm Gauss-Jordan 
		Point pivot = findPivot(startIndex); //finding Pivot
		
		
		if(startIndex == pivot.x && startIndex == pivot.y) { //if Pivot was found on the right place
			return;
		}
		
		
		if(startIndex != pivot.x) { //changing verses 
			//swaping values from the FreeTerms array
			double c =  FreeTerms[pivot.x];
			FreeTerms[pivot.x] = FreeTerms[startIndex];
			FreeTerms[startIndex] = c;
			
			//swaping all values from the Matrix between startIndex's verse and pivot's verse
			for(int i = startIndex; i < SIZE; i++) {
				c = Matrix[startIndex][i];
				Matrix[startIndex][i] = Matrix[pivot.x][i];
				Matrix[pivot.x][i] = c;
			}
			pivot.x = startIndex;
			
			
		}
		
		if(startIndex != pivot.y) { //changing columns
			//swaping values from the Unknowns array
			double c = Unknowns[pivot.y];
			Unknowns[pivot.y] = Unknowns[startIndex];
			Unknowns[startIndex] = c;
			
			//swaping all values from the Matrix between startIndex's verse and pivot's column
			for(int i = startIndex; i < SIZE; i++) {
				c = Matrix[i][startIndex];
				Matrix[i][startIndex] = Matrix[i][pivot.y];
				Matrix[i][pivot.y] = c;
			}
			pivot.y = startIndex;
		}
	}
	
	
	//method which finds where is pivot (element of Matrix, or sub-matrix where startIndex !=0, with maximum value)
	public static Point findPivot(int startIndex) {
		Point result = new Point(startIndex,startIndex, Math.abs(Matrix[startIndex][startIndex]));
		
		for(int i = startIndex; i < SIZE; i++) {
			for(int j = startIndex; j < SIZE; j++) {
				if(Math.abs(Matrix[i][j]) > result.value){
					result.value = Math.abs(Matrix[i][j]);
					result.x = i;
					result.y = j;
				}
			}
		}
				
		return result;
	}
	
	public static void draw() { //helping method, good to draw small matrixes 
		for(int i = 0; i < SIZE-1; i++) {
			System.out.print(Unknowns[i] + " ");
		}
		System.out.println(Unknowns[SIZE-1]);
	
		
		for(int i = 0; i < SIZE; i++) {
			for(int j = 0; j < SIZE; j++) {
				System.out.print(Matrix[i][j] + "(-------------)");
			}
			System.out.println("||||| " + FreeTerms[i]);
		}
	}
	
	public static void gauss () { //the most important method, uses other ones to implement gauss-jordan algorithm on the matrix
		int startIndex = 0;
		while(startIndex < SIZE){
			swapPivot(startIndex);
			double ratio;
		
			for(int i = 0; i < SIZE; i++) {
				if(i == startIndex && i == SIZE - 1){
					return;
				}
			
				if(i == startIndex) {
					i++;
				}
			
				ratio = ((Matrix[i][startIndex]) / (Matrix[startIndex][startIndex]));
				FreeTerms[i] = FreeTerms[i] * ratio - FreeTerms[startIndex];
			
				for(int j = startIndex; j < SIZE; j++) {
					Matrix[i][j] = Matrix[startIndex][j] * ratio - Matrix[i][j];
				}
			}	
		startIndex++;
		}
	}
	
	public static void clear() { //writes 1.0 on the diagonal and updates values of free terms
		for(int i = 0; i < SIZE; i++) {
			FreeTerms[i] = FreeTerms[i] / Matrix[i][i];
			Matrix[i][i] = 1.0;
		}
	}
	
	public static void writeUnknowns() {
		double[] result = new double[SIZE];
		for(int i = 0; i < SIZE; i++) {
			result[(int)Unknowns[i]] = FreeTerms[i];
		}
		for(int i = 0; i < SIZE; i++) {
			System.out.println("Value of x" + i + " = " + result[i] + ".");
		}
	}
	

	public static void main(String[] args) {
		prepareMatrixes();
		RealMatrix A = new Array2DRowRealMatrix(libMatrix);
		
		long startTime = System.nanoTime();
		gauss();
		clear();
		writeUnknowns();
		long estimatedTime = System.nanoTime() - startTime;
		System.out.println("Program have been working for " + estimatedTime + "  nanoseconds");
			
	}
}
