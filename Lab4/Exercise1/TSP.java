package Lab4;

import java.applet.Applet;
import java.awt.Dimension;
import java.awt.Graphics;
import java.io.File;
import java.io.PrintWriter;
import java.util.Random;
import org.apache.commons.math3.distribution.NormalDistribution;

public class TSP extends Applet implements Runnable {
	Random r = new Random();
	Thread t; //thread for drawings
	
	int SIZE = 800; //size of the applet's window
	int N = 100; //number of points to draw
	double startTemp = 100; //initial temperature
	double coolingRate = 0.99999;
	
	
	double currTemp; //current temperature
	double totalDistance; // summary distance of the current road
	double firstDistance; // summary distance of the first road
	boolean consecutive = false; //flag if true - changing arbitrary swap into consecutive swap
	
	Point[] points;
	
	
	public void start(){
		t = new Thread(this);
		t.start();
	}
	
	public void stop(){
		t = null;
	}
	
	public void init (){
		this.setSize(new Dimension(SIZE,SIZE));
	}
	
	
	private double calculateTheDistance(Point a, Point b) { //function to calculate distance between two points
		return(Math.sqrt(Math.pow(a.x - b.x, 2) + Math.pow(a.y - b.y, 2)));	
	}
	
	private double calculateTotalDistance(Point[] points){
		double result = 0.0;
		for(int i = 0; i < N -1; i++) {
			result = result + calculateTheDistance(points[i], points[i+1]);
		}
		return (result + calculateTheDistance(points[0], points[N-1]));
	}
	
	//---------------------------------------------------------------------------------
	//Methods preparing various kinds of distributions:
	
	private Point[] prepareUniformDistribution(Point[] points) {
		for(int i = 0; i < N; i++) {
			points[i] = new Point(r.nextInt(SIZE - 100) + 50, r.nextInt(SIZE - 100) + 50);
		}
		return points;
	}
	
	private Point[] prepareNormalDistribution(Point[] points) {
		NormalDistribution d1 = new NormalDistribution(300,300);
		for(int i = 0; i < N; i++) {
			points[i] = new Point((int)Math.abs(d1.sample())+50, (int)Math.abs(d1.sample()) + 50);
		}
		return points;
	}
	
	
	private Point[] prepareGroupDistribution(Point[] points) {
		for(int i = 0; i < N; i ++) {
			int x = r.nextInt(50);
			int y = r.nextInt(50);
			int modulo = i%9;
			
			if(modulo >= 0 && modulo <= 2) {
				points[i] = new Point(x + 50, y + 50 + 200 * modulo);
			}
			
			if(modulo >= 3 && modulo <=5) {
				points[i] = new Point(x + 250, y + 50 + 200 * (modulo -3));
			}
			
			if(modulo >= 6 && modulo <=8) {
				points[i] = new Point(x + 450, y + 50 + 200 * (modulo - 6));
			}
			
		}
		
		return points;
	}
	
	//----------------------------------------------------------------------------------
	
	private double[][] prepareDistancesArray(double[][] distances, Point[] points) {
		for(int i = 0; i < N; i++) {
			for(int j = 0; j < N; j++){
				distances[i][j] = calculateTheDistance(points[i], points[j]);
			}
		}
		return distances;
	}
	
	
	public Point[] changeOrder(Point[] points, boolean isConsecutive, int iterationCounter){ //swaping two points in the current road
		Point [] nextOrder = new Point[N];
		int a, b;
		for(int i =0; i < N; i++){
			nextOrder[i] = new Point(points[i].x,points[i].y);
		}
		
		if(isConsecutive == true) {
			a = iterationCounter%N;
			b = (iterationCounter + 1)%N;		
			
		}
		
		else {
			a = r.nextInt(N);
			b = r.nextInt(N);
		}
			Point c = new Point(nextOrder[a].x,nextOrder[a].y);
			nextOrder[a].x = nextOrder[b].x;
			nextOrder[a].y = nextOrder[b].y;
			nextOrder[b].x = c.x;
			nextOrder[b].y = c.y;
		
		return nextOrder;
		
	}

	public double anneal(Point[] points, double T0, boolean isConsecutive) {
		Point[] nextOrder; 
	    int iterationCounter = 0;
	    currTemp = T0; 
	   
	    double distanceDifference = 0; 
	    double minTemperature = 0.01;
	    double distance = firstDistance;
	    
	    try {
			PrintWriter writer = new PrintWriter(new File("toDraw.txt"));
			writer.println(distance);
			while (currTemp > minTemperature){
		    	nextOrder = changeOrder(points, isConsecutive, iterationCounter);
		        distanceDifference = calculateTotalDistance(nextOrder) - distance;
		        
		        if ((distanceDifference < 0) || (distance > 0 && Math.exp(-distanceDifference / currTemp) > r.nextDouble())){
		            for (int i = 0; i < nextOrder.length; i++){
		                points[i] = new Point(nextOrder[i].x,nextOrder[i].y);
		            }
		            distance = distanceDifference + distance;
		            writer.println(distance);
		        }
		        currTemp = currTemp * coolingRate;
		        //currTemp = startTemp - coolingRate * iterationCounter;
		        iterationCounter++;
		    }
			writer.close();
		}catch (Exception e) {System.out.println(e.getMessage());}	
	    
	   
	   System.out.println("Amount of iterations : " + iterationCounter);
	   System.out.println("Final distance = " + distance);
	   return distance;
	}
	
	
	public void run(){
		while(t == Thread.currentThread()){
			points = new Point[N]; //array of all points
			
			points = prepareUniformDistribution(points); //creating points in chosen distribution
			firstDistance = calculateTotalDistance(points); //distance of the starting path
			System.out.println("Starting distance = " + firstDistance);
			
			
			currTemp = startTemp;
			repaint(); //drawing the state before algorithm starts
			totalDistance = anneal(points, startTemp, consecutive);		
			repaint(); //drawing the state after algorithm work
			stop();
		}
	}

	public void paint(Graphics g){
		for(int i = 0; i < N; i++){
			g.fillOval(points[i].x, points[i].y, 5, 5);  
		}
		
		for(int i = 0; i < N - 1; i++) {
			g.drawLine(points[i].x, points[i].y, points[i+1].x, points[i+1].y);
		}
		g.drawLine(points[0].x, points[0].y, points[N-1].x, points[N-1].y);
		
		g.drawString("Starting length = " + firstDistance, 10, 10);
		g.drawString("Finish length = "+totalDistance,300,10);
	    g.drawString("Starting temperature = "+startTemp,10,25);
	    g.drawString("Finish temperature = "+ currTemp,300,25);
	    g.drawString("N = " + N, 10, 40);
	    g.drawString("Distribution: Uniform", 300, 40);
		
	}
}
