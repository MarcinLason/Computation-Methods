package Lab1;
import java.lang.Math;

public class RoundingErrors {
	
	static double sinusNormal(double x) {
		return x - Math.sin(x);
	}
	
	static double sinusBetter(double x) {
		return (( (x * x) - 1 + (Math.cos(x) * Math.cos(x)))/(x + Math.sin(x)));
	}
	
	static double cosinusNormal(double x) {
		return 1 - Math.cos(x);
	}
	
	static double cosinusBetter(double x) {
		return ( (Math.sin(x) * Math.sin(x))/ (1 + Math.cos(x)) );
	}

	public static void main(String[] args) {
		double StartingX = 0.000001;
		double RateofGrowth = 0.000001;
		
		
		for(int i = 0; i < 10; i++) {
			System.out.println("------------------------------------");
			System.out.println("X = " + StartingX + " end increases by " + RateofGrowth + " .");
			System.out.println("a) Subtraction normally:           " + sinusNormal(StartingX));
			System.out.println("a) Subtraction in better version:  " + sinusBetter(StartingX));
			System.out.println("b) Subtraction normally:           " + cosinusNormal(StartingX));
			System.out.println("b) Subtraction in better version:  " + cosinusBetter(StartingX));
			
			StartingX = StartingX + RateofGrowth;
		}
		

	}

}
