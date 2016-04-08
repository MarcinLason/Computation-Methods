package Lab1;

public class SubtractionErrors {

	static long factorial (int x) {
		if(x == 0) {
			return 1;
		}
		return x * factorial(x-1) ;
	}
	
	static double mySinus (double x, int precision) {
		double sinus = 0.0;
		for(int i = 0; i < precision; i++){
			sinus = sinus + ( Math.pow((-1.0), i) * (Math.pow(x, 2 * i + 1) / factorial(2 * i + 1)));
		}
		return sinus;
	}
	
	static double myCosinus (double x, int precision) {
		double cosinus = 0.0;
		for(int i = 0; i < precision; i++){
			cosinus = cosinus + ( Math.pow((-1.0), i) * (Math.pow(x, 2 * i) / factorial(2 * i)));
		}
		return cosinus;
	}
	
	static double myBetterSinus (double x, int precision) {
		double positive = 0.0;
		double negative = 0.0;
		
		for(int i = 0; i < precision; i++){
			
			if(i%2 == 0) {
				positive = positive + ( Math.pow((-1.0), i) * (Math.pow(x, 2 * i + 1) / factorial(2 * i + 1)));
			}
			if(i%2 == 1) {
				negative = negative + ( Math.pow((-1.0), i) * (Math.pow(x, 2 * i + 1) / factorial(2 * i + 1)));
			}
		}
		return negative + positive;
	}
	
	static double myBetterCosinus (double x, int precision) {
		double positive = 0.0;
		double negative = 0.0;
		
		for(int i = 0; i < precision; i++){
			
			if(i%2 == 0) {
				positive = positive + ( Math.pow((-1.0), i) * (Math.pow(x, 2 * i) / factorial(2 * i)));
			}
			if(i%2 == 1) {
				negative = negative + ( Math.pow((-1.0), i) * (Math.pow(x, 2 * i) / factorial(2 * i)));
			}
		}
		return negative + positive;
	}
	
	static double sinusNormal(double x) {
		return x - Math.sin(x);
	}
	
	static double sinusBetter(double x) {
		return x - mySinus(x, 20);
	}
	
	static double sinusTheBest(double x) {
		return x - myBetterSinus(x,20);
	}
	
	static double cosinusNormal(double x) {
		return 1 - Math.cos(x);
	}
	
	static double cosinusBetter(double x) {
		return 1 - myCosinus(x,20);
	}
	
	static double cosinusTheBest(double x) {
		return 1 - myBetterCosinus(x,20);
	}

	public static void main(String[] args) {
		double StartingX = 0.1;
		double RateofGrowth = 0.01;
		
		
		for(int i = 0; i < 10; i++) {
			System.out.println("------------------------------------");
			System.out.println("X = " + StartingX + " end increases by " + RateofGrowth + " .");
			System.out.println("a) Subtraction normally:            " + sinusNormal(StartingX));
			System.out.println("a) Subtraction in better version:   " + sinusBetter(StartingX));
			System.out.println("a) Subtraction in the best version: " + sinusTheBest(StartingX));
			System.out.println("b) Subtraction normally:            " + cosinusNormal(StartingX));
			System.out.println("b) Subtraction in better version:   " + cosinusBetter(StartingX));
			System.out.println("b) Subtraction in the best version: " + cosinusTheBest(StartingX));
			
			StartingX = StartingX + RateofGrowth;
		}
		
	}
}
