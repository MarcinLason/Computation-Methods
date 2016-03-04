package Lab1;
import java.lang.Math;


public class PartialSums{
	
	public float dzetaFloat (float s, int n, boolean fromOne) {
		float result = (float) 0.0;
		
		if(fromOne == true) {
			for(int k = 1; k <= n; k++) {
				result = result + (float)(1/(Math.pow(k, s)));
			}
		}
		
		if(fromOne == false) {
			for(int k = n; k > 0; k--) {
				result = result + (float)((1/(Math.pow(k, s))));
			}
		}
		
		return result;
	}
	
	
	public float etaFloat (float s, int n, boolean fromOne) {
		float result = (float) 0.0;
		
		if (fromOne == true) {
			for(int k = 1; k <=n; k++) {
				result = result + (float)(Math.pow((float)(-1.0), (k-1)) * (1/(Math.pow(k, s))) ); 
			}
		}
		
		if(fromOne == false) {
			for(int k = n; k > 0; k--) {
				result = result + (float)(Math.pow((float)(-1.0), (k-1)) * (1/(Math.pow(k, s))) ); 
			}
		}
		
		return result;
	}
	
	

	public double dzetaDouble (double s, int n, boolean fromOne) {
		double result = 0.0;
		
		if(fromOne == true) {
			for(int k = 1; k <= n; k++) {
				result = result + (1/(Math.pow(k, s)));
			}
		}
		
		if (fromOne == false) {
			for(int k = n; k > 0; k--) {
				result = result + (1/(Math.pow(k, s)));
			}
		}
		
		return result;
		
	}
	
	
	public double etaDouble (double s, int n, boolean fromOne) {
		double result = 0.0;
		if (fromOne == true) {
			for(int k = 1; k <=n; k++) {
				result = result + (Math.pow((-1.0), (k-1)) * (1/(Math.pow(k, s))) ); 
			}
		}
		
		if(fromOne == false) {
			for(int k = n; k > 0; k--) {
				result = result + (Math.pow((-1.0), (k-1)) * (1/(Math.pow(k, s))) ); 
			}
		}
		return result;
	}
	
	public static void main(String[] args) {
		int [] N = {50, 100, 200, 500, 1000};
		double [] S = {2,3.6667, 5, 7.2, 10};
		PartialSums x =  new PartialSums();

		
		for(int i = 0; i < N.length; i++){
			for(int j = 0; j < S.length; j++) {
				System.out.println("-------------------------------------------------------------------");
				System.out.println("N = " + N[i]);
				System.out.println("S = " + S[j]);
				System.out.println("dzetaFloat forward:       " + x.dzetaFloat((float)S[j], N[i], true));
				System.out.println("dzetaFloat from the end:  " + x.dzetaFloat((float)S[j], N[i], false));
				System.out.println("etaFloat forward:         " + x.etaFloat((float)S[j], N[i], true));
				System.out.println("etaFloat from the end:    " + x.etaFloat((float)S[j], N[i], false));
				System.out.println("dzetaDouble forward:      " + x.dzetaDouble(S[j], N[i], true));
				System.out.println("dzetaDouble from the end: " + x.dzetaDouble(S[j], N[i], false));
				System.out.println("etaDouble forward:        " + x.etaDouble(S[j], N[i], true));
				System.out.println("etaDouble from the end:   " + x.etaDouble(S[j], N[i], false));
				System.out.println("-------------------------------------------------------------------");
			}
		}
		
		
		

	}

}