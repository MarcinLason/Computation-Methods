package Lab4;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Random;
import java.util.Scanner;

public class Sudoku {
	static Random r = new Random();
	static int[][] Board = new int[9][9]; //current sudoku's Board
	static boolean mutableSpaces[][] = new boolean[9][9]; //false means that I can not change this space
	
	private static void prepareBoard(String sudokuPath){ 
		boolean CurrEmpty[][] = new boolean[9][9]; // which fields are still empty 
		int InitializedNumbers[] = new int[10]; // how many times each digit occurs in initial board
		Scanner scanner;
	     try {
			scanner = new Scanner(new File(sudokuPath));
			int x = 0;
			while(scanner.hasNextLine()){
				String line = scanner.nextLine();
			
				for (int y = 0; y < 9; y++){
					if(line.charAt(y) == 'X'){
						Board[x][y] = 0;
						CurrEmpty[x][y] = true;
						mutableSpaces[x][y] = true;
					}
					else{
						char symbol = line.charAt(y);
						int number = Integer.parseInt(String.valueOf(symbol));
						Board[x][y] = number;
						InitializedNumbers[number]++;
					}
				}
				x++;
			}
			scanner.close();
	     } catch (FileNotFoundException e) {
		System.out.println("Wrong path to Sudoku.");
	     }
	     System.out.println("Starting board:");
	     drawBoard();
	     System.out.println("\n\n");
	     
	     for(int i = 1; i < 10; i++){
			 int missing = 9- InitializedNumbers[i];
			
			 for(int j=0; j<missing;j++){
				 int x = r.nextInt(9);
				 int y = r.nextInt(9); 
				
				 while(!CurrEmpty[x][y]){
					x = r.nextInt(9);
					y = r.nextInt(9); 
				}
				Board[x][y] = i;
				CurrEmpty[x][y] = false;
			 }
	     }
}
	
	private static int getValue(int[][]board){
		return calculateColumnsValue(board) + calculateRowsValue(board) + calculateSquaresValue(board);
	}
	
	private static int calculateColumnsValue(int [][] board){	
		int sum = 0;
		for(int i = 0; i < 9; i++){
			int counter = 0;
			int contained[] = new int[10];
			
			for(int j = 0; j < 9; j++){
				contained[board[j][i]]++;
			}
				
			for(int j = 1; j < 10; j++){
				if(contained[j]>0){
					counter = counter + contained[j] - 1;
				}	
			}	
			sum = sum + counter;
		}
		return sum;
	}
	
	private static int calculateRowsValue(int [][] board) {
		int sum = 0;
		for(int i = 0; i < 9; i++) {
			int counter = 0; 
			int contained[] = new int[10];
			
			for(int j = 0 ; j<9 ; j++) {
				contained[board[i][j]]++;
			}
				
			for(int j = 1; j < 10; j++) {
				if(contained[j] > 0){
					counter = counter + contained[j]-1;
				}	
			}
			sum = sum + counter;	
		}
		return sum;
	}
	
	private static int calculateSquaresValue(int [][] board){
		int sum = 0;
		for(int x = 0; x < 9; x = x+3){
			for(int y = 0; y < 9; y = y +3) {
				int counter = 0; 
				int contained[] = new int[10];
				
				for(int i = x; i < x+3; i++) {
					for (int j = y; j < y+3; j++){
						contained[board[i][j]]++;
					}
				}
					
				for(int i = 1; i < 10; i++) {
					if(contained[i] > 0) {
						counter = counter + contained[i] - 1;
					}	
				}
				sum = sum + counter;
			}
		}
		return sum;
	}
	
	 static void anneal(String path, double startTemp)
	{		 
		int [][] nextState;
	    int iterationCounter = -1;

		double currTemp = startTemp;
	    int difference;
	    double coolingRate = 0.9999999;
	    double minTemp = 0.01;

	    int value = getValue(Board);
	    System.out.println("Starting value: "+value);
		    while (currTemp > minTemp && value > 0){	    	
		    	nextState = getNext(Board);
		        difference = getValue(nextState) - value;
	
		        if ((difference < 0) || value > 0 && (Math.exp(-difference / currTemp) > r.nextDouble())){
		            for (int i = 0; i < 9; i++){
		            	for( int j=0 ; j<9 ; j++){
		            		Board[i][j] = nextState[i][j];
		            	}        
		            }
		            value = difference + value;
		        }
		        currTemp = currTemp * coolingRate;
		        iterationCounter++;
		    }
	   System.out.println("Amount of iterations: " + iterationCounter);
	   System.out.println("Solved sudoku:");
	   drawBoard();
	   System.out.println("Final value = "+value);
	}
	
	
	private static int[][] getNext(int[][] currBoard) {
		int [][] newBoard = new int [9][9];
		
		 for (int i = 0; i < 9; i++){
			 for (int j = 0; j < 9; j++){
				 newBoard[i][j] = currBoard[i][j];
			 }	 
		 }
			 
		 int x1 = r.nextInt(9);
		 int y1 = r.nextInt(9);
		 int x2 = r.nextInt(9);
		 int y2 = r.nextInt(9);
		
		
		 while(!mutableSpaces[x1][y1]){
			 x1 = r.nextInt(9);
			 y1 = r.nextInt(9);
		 }
		 
		 while(!mutableSpaces[x2][y2]){
			 x2 = r.nextInt(9);
			 y2 = r.nextInt(9);
		 }
		 
		int tmp = newBoard[x1][y1];
		newBoard[x1][y1] = newBoard[x2][y2];
		newBoard[x2][y2] = tmp;
		return newBoard;
	}
	
	private static void drawBoard() { //helping method drawing the current state of Board
		for(int i = 0; i < 9; i++) {
			for(int j = 0; j < 8; j++){
				System.out.print(Board[i][j] + " ");
			}
			System.out.println(Board[i][8]);
		}
	}

	public static void main(String[] args) {
		String path = "sudoku.txt";
		prepareBoard(path);	
		anneal(path,300);
	}
}

