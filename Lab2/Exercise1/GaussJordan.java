package Lab2;

import java.util.Random;


public class GaussJordan {
	final static int SIZE = 500;
	static double[][] Matrix = new double[SIZE][SIZE];
	static double[] JakisDopelniacz = new double[SIZE];
	
	public static void fillMatrix() {
		Random rand = new Random();
		for(int i = 0; i < SIZE; i++) {
			for(int j = 0; j < SIZE; j++) {
				Matrix[i][j] = rand.nextDouble();
			}
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

	public static void main(String[] args) {
		
	}
}
