package Lab1;
import java.lang.Math;

import com.panayotis.gnuplot.JavaPlot;
import com.panayotis.gnuplot.JavaPlot.Key;
import com.panayotis.gnuplot.plot.DataSetPlot;
import com.panayotis.gnuplot.style.NamedPlotColor;
import com.panayotis.gnuplot.style.PlotStyle;
import com.panayotis.gnuplot.style.Style;

public class RoundingErrors {
	public static double[][] resultsD = new double[500][2];
	public static float[][] resultsF = new float[500][2];
	
	
	public static void setResultsD (double x0, double r) { //method which write down next values of x to array resultsD
		resultsD[0][0] = r;
		resultsD[0][1] = x0;
		
		for(int n = 0; n < (resultsD.length - 1); n++) {
			resultsD[n+1][0] = r;
			resultsD[n+1][1] = r * resultsD[n][1] * (1 - resultsD[n][1]);
		}
		
	}
	
	public static void setResultsF (float x0, float r) { //method which write down next values of x to array resultsF
		resultsF[0][0] = r;
		resultsF[0][1] = x0;
		
		for(int n = 0; n < (resultsF.length - 1); n++) {
			resultsF[n+1][0] = r;
			resultsF[n+1][1] = r * resultsF[n][1] * (1 - resultsF[n][1]);
		}
		
	}
	
	public static void plot(double x0) { // method which create a diagram of all x for r from range <1.0, 4.0> (Bifurcation diagram)
		JavaPlot plot = new JavaPlot();
		PlotStyle myPlotStyle = new PlotStyle();
		myPlotStyle.setStyle(Style.DOTS);
		myPlotStyle.setLineType(NamedPlotColor.BLACK);
		
		
		for(double r = 1.0; r < 4.0; r+= 0.002){
			setResultsD(x0, r); //I prepare new x-es to plot
			DataSetPlot dsp = new DataSetPlot(resultsD);
	        dsp.setPlotStyle(myPlotStyle);
	        plot.addPlot(dsp); //Adding new points which will be plotted on the diagram
	        System.out.println(r);
		}
		
        plot.setKey(Key.OFF);
        plot.plot(); //final plotting all of the points
	}
	
	public static void punctualPlot(double x0) { //method which will compare doubles and floats values on the small range of r
		JavaPlot plot = new JavaPlot();
		
		PlotStyle styleDouble = new PlotStyle();
		styleDouble.setStyle(Style.DOTS);
		styleDouble.setLineType(NamedPlotColor.BLUE);
		
		PlotStyle styleFloat = new PlotStyle();
		styleFloat.setStyle(Style.DOTS);
		styleFloat.setLineType(NamedPlotColor.RED);
		
		for(double r = 3.75; r < 3.8; r+= 0.001){
			setResultsD(x0, r);
			DataSetPlot dsp = new DataSetPlot(resultsD);
	        dsp.setPlotStyle(styleDouble);
	        plot.addPlot(dsp);
	        System.out.println(r); //only for the view that program is running
		}
		
		for(float r = 3.75f; r < 3.8f; r+= 0.001) {
			setResultsF((float)x0, r);
			DataSetPlot dsp = new DataSetPlot(resultsF);
			dsp.setPlotStyle(styleFloat);
			plot.addPlot(dsp);
			System.out.println(r); //only for the view that program is running
		}
		
        plot.setKey(Key.OFF);
        plot.plot();
	}
	
	public static int operationCounter(float x0) {
		int counter = 0;
		float r = 4.0f;
		float x = x0;
		
		while(Math.abs(x) > 0.0001f) {
			x = r * x * (1-x);
			counter++;
		}
		return counter;
		
	}
	

	
	public static void main(String[] args) {
		//Exercise a):
		/*
		plot(0.3);
		plot(0.6);
		plot(0.9);
		*/
		
		//Exercise b):
		/*
		punctualPlot(0.3);
		*/
		
		//Exercise c):
		for(double x = 0.1; x < 1.0; x+= 0.1){
			System.out.println("Amuount of operations for " + (float)x + ": " + operationCounter((float)x));
		}
		
	}
	
	

}
