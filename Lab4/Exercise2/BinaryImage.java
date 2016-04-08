package Lab4;

import java.applet.Applet;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Random;


public class BinaryImage extends Applet implements Runnable {
	Thread t; //thread for drawings
	Random r = new Random();
	boolean paintFlag = false; //where true image is ready to painting
	
	int SIZE = 300; //size of the image
	double startTemp = 400; //initial temperature
	double density = 0.3; //density of the image
	int range = 3;
	String neighbourhood = "cross"; //values: diagonal/cross/eight
	
	double energyValue; //current value of energy
	double currTemp; //current temperature
	boolean[][] currImage;
	boolean[][] startImage;
	int [] randomPoints= new int[16];
	
	public boolean[][] createImage(){
		boolean[][] image = new boolean[SIZE][SIZE];
		int x;
		int y;
		int blackCounter = 0;
		for(int i = 0; i < SIZE; i++){
			for (int j = 0; j < SIZE; j++) {
				image[i][j] = false;
			}
		}
			
		while(blackCounter < SIZE * SIZE * density){
			 x = r.nextInt(SIZE);
			 y = r.nextInt(SIZE);
			 if(image[x][y] == false){
				 image[x][y] = true;
				 blackCounter++;
			 }
		}			
		return image;
	}
	
	public void prepareStartImage() {
		startImage = new boolean[SIZE][SIZE];
		for (int i = 0; i < SIZE; i++){
			for (int j = 0; j < SIZE; j++){
				startImage[i][j] = new Boolean(currImage[i][j]);
			}	
		}
	}
	
	public boolean[][] createNextImage(){
		boolean [][] nextImage = new boolean[SIZE][SIZE];
		
		for(int i = 0; i < SIZE; i++){
			for(int j = 0; j < SIZE; j++) {
				nextImage[i][j] = new Boolean(currImage[i][j]);
			}
		}
		
		for(int i = 0; i < 16; i++){
			randomPoints[i]= r.nextInt(SIZE);
		}
		
		boolean tmp = false;
		for(int i = 0; i < 4; i++){
			tmp = new Boolean(nextImage[randomPoints[i]][randomPoints[i+4]]);
			nextImage[randomPoints[i]][randomPoints[i+4]] = nextImage[randomPoints[i+8]][randomPoints[i+12]];
			nextImage[randomPoints[i+8]][randomPoints[i+12]] = tmp;
		}
		return nextImage;
	}
	
	public void annealing()
	{
		boolean [][] nextImg;
	    int iterationCounter = -1;
	    int difference = 0;
	    double alfa = 0.99999;
	    double minTemperature = 0.01;
	    
	    currTemp = startTemp;
	    energyValue = calculateTotalEnergy();
	   	
	    while (currTemp > minTemperature)
	    {			
	    	nextImg = createNextImage();
	    	difference = calculateNewEnergy(nextImg);
	       
	       if ((difference < 0) || (difference > 0 && Math.exp(-difference / currTemp) > r.nextDouble())){
	    	   for (int i = 0; i < SIZE; i++){
	    		   for (int j = 0; j < SIZE; j++) {
	    			   currImage[i][j] = new Boolean(nextImg[i][j]); 
	    		   }		
	    	   } 	 
	           energyValue = difference + energyValue;
	        }
	        currTemp = currTemp * alfa;
	        iterationCounter++;
	    }
	    System.out.println("Iterations: " + iterationCounter);
	    System.out.println("Finish energy: " + energyValue);
		paintFlag = true;  
	}
	
	public boolean checkDiagonalNeighbourhood(int a, int b, int x, int y){
		if(Math.abs(a-x) == Math.abs(b-y) && b != y){
			return true;
		}
		return false;	
	}
	
	public boolean checkCrossNeighbourhood(int a, int b, int x, int y){
		if(a == x || b == y){
			return true;
		}
		return false;	
	}
	
	public boolean checkEightNeighbourhood(int a, int b, int x, int y){
		if(checkCrossNeighbourhood(a,b,x,y)|| checkDiagonalNeighbourhood(a,b,x,y))
			return true;
		return false;	
	}
	
	public boolean checkNeighbourhood(int a, int b, int x, int y) {
		if(neighbourhood == "diagonal") {
			return checkDiagonalNeighbourhood(a,b,x,y);
		}
		
		if(neighbourhood == "cross") {
			return checkCrossNeighbourhood(a,b,x,y);
		}
		
		if(neighbourhood == "eight") {
			return checkEightNeighbourhood(a,b,x,y);
		}
		
		else {
			System.out.println("You have to define correct neighbourhood!");
			return false;
		}
		
	}
	
	public int setEnergy(boolean[][]img, int a, int b, int c, int d ){
		int energy = 0;
		if((img[a][b] && img[c][d]) && (a!=c || b!=d)) {
			energy = -50;
		}
		
		if((img[a][b] == false && img[c][d] == false) && (a!=c || b!=d)) {
			energy = - 50;
		}
			
		return energy;
	}
	
	public int calculateTotalEnergy(){ //getting total energy of the current image
		int result = 0;
		
		for(int i = 0; i < SIZE; i++) { 
			for(int j = 0; j < SIZE; j++){
				int horizontal = i - range;
				if (horizontal < 0) {
					horizontal = 0;
				}
					
				while(horizontal < i + range && horizontal < SIZE){
					int vertical = j - range;
					if (vertical < 0) {
						vertical = 0;
					}
						
					while(vertical < j + range && vertical < SIZE){
						int energy = 0;
						if (checkNeighbourhood(i,j,horizontal,vertical)) {
							 energy = setEnergy(currImage,i,j,horizontal,vertical);
						}
						result = result + energy;
						vertical++;
					}
					horizontal++;
				}	
			}
		}
		return result;
	}
	
	public int calculateNewEnergy(boolean[][] newImg ){
      int result = 0;
	  for(int j = 0; j < 4; j++){
		  int x1 = randomPoints[j];	
		  int y1 = randomPoints[j+4]; 
		  int x2 = randomPoints[j+8];
		  int y2 = randomPoints[j+12]; 
		  int horizontal = x1 - range;
		  
		  if (horizontal < 0) {
			  horizontal = 0;
		  }
			
		  while(horizontal < x1 + range && horizontal<SIZE){
			  int vertical = y1 - range;
			  if (vertical < 0){
				  vertical = 0; 
			  }
			
			  while(vertical < y1 + range && vertical <SIZE){
				  int oldEnergy = 0;
				  int newEnergy = 0;
				  if (checkNeighbourhood(x1,y1,horizontal,vertical)){
					 newEnergy = setEnergy(newImg,x1,y1,horizontal,vertical);
					 oldEnergy = setEnergy(currImage,x1,y1,horizontal,vertical);
				  }
				  result = result + newEnergy;
				  result = result - oldEnergy;
				  vertical++;
			}
			horizontal++;
		  }
		  
		  horizontal = x2 - range;
		  if (horizontal < 0) {
			  horizontal = 0; 
		  }
				
		  while(horizontal < x2 + range && horizontal <SIZE){
			  int vertical = y2 - range;
			  if (vertical < 0) {
				  vertical = 0; 
			  }
					
			  while(vertical < y2 + range && vertical < SIZE){
				  int oldEnergy = 0;
				  int newEnergy = 0;
				  if (checkNeighbourhood(x2,y2,horizontal,vertical)){
						 newEnergy = setEnergy(newImg,x2,y2,horizontal,vertical);
						 oldEnergy = setEnergy(currImage,x2,y2,horizontal,vertical);
				  }
				  result = result + newEnergy ;
				  result = result - oldEnergy;
				  vertical++;
			  }
			  horizontal++;
		  }	
	  }
	return result;	
	}
		
	public void start(){
		t = new Thread(this);
		t.start();
	}
	
	public void stop(){
		t = null;
	}
	
	public void init (){
		this.setSize(new Dimension(1000, 1000));
	}
	
	
	@Override
	public void run() {
		currImage = createImage();
		prepareStartImage();
		annealing();		
		repaint();
		stop();
	}
	
	 public void paint(Graphics g) {
		 g.drawString("Total energy: " + energyValue, 20,15);
	     g.drawString("Initial temperature: " + startTemp, 20,35);
	     g.drawString("Current temperature: " + currTemp, 20,55);
		 
	     if(paintFlag){
	    	g.drawString("Original image: ", 20, 85);
	    	g.drawString("Changed image: ", 70 + SIZE, 85);
	    	 
	    	for (int i = 0; i < SIZE; i++){
	    		for (int j = 0; j < SIZE; j++){
	    			if(startImage[i][j] == true) {
	    				g.fillOval((i+20),(j+100),1,1);
	        		}		
	        	}
	        }    
	 
	        for(int i=0;i<SIZE;i++) {
	        	for(int j=0 ; j<SIZE ; j++){
	        		if(currImage[i][j]) {
	        			g.fillOval((70 + SIZE + i),(100 + j),1,1);
	        		}		
	        	}
	        }        
	     }  
	 }
}
