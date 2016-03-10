package Lab2;

import java.util.Random;


public class GaussJordan {
	final static int SIZE = 4;
	static double[][] Matrix = new double[SIZE][SIZE];
	static double[] FreeTerms = new double[SIZE];
	static double[] Unknowns = new double[SIZE];
	
	public static void fillMatrix() {
		Random rand = new Random();
		for(int i = 0; i < SIZE; i++) {
			for(int j = 0; j < SIZE; j++) {
				Matrix[i][j] = rand.nextDouble();
			}
		}
		
	}
	
	public static void fillFreeTerms() {
		Random rand = new Random();
		for(int i = 0; i < SIZE; i++){
			FreeTerms[i] = rand.nextDouble();
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
			System.out.println("Nie muszê nic zamieniaæ");
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
		Point result = new Point(startIndex,startIndex, Matrix[startIndex][startIndex]);
		
		for(int i = startIndex; i < SIZE; i++) {
			for(int j = startIndex; j < SIZE; j++) {
				if(Matrix[i][j] > result.value){
					result.value = Matrix[i][j];
					result.x = i;
					result.y = j;
				}
			}
		}
				
		return result;
	}
	
	public static void draw() {
		for(int i = 0; i < SIZE-1; i++) {
			System.out.print(Unknowns[i] + " ");
		}
		System.out.println(Unknowns[SIZE-1]);
	
		
		for(int i = 0; i < SIZE; i++) {
			for(int j = 0; j < SIZE; j++) {
				System.out.print(Matrix[i][j] + " ");
			}
			System.out.println("|| " + FreeTerms[i]);
		}
	}

	public static void main(String[] args) {
		prepareMatrixes();
		draw();
		Point p = findPivot(0);
		System.out.println("" + p.value + "     " + p.x + "      " + p.y);
		
		swapPivot(0);
		swapPivot(1);
		swapPivot(2);
		swapPivot(3);
		draw();
		
	
	
	
	
	}
}
