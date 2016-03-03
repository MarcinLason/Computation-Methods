package Lab1;
import java.lang.Math;


public class SumyCzesciowe {
	
	public float dzetaFloat (float s, int n, boolean fromOne) {
		float result = (float) 0.0;
		
		if(fromOne == true) {
			for(int k = 1; k <= n; k++) {
				result = (float) (result + (1/(Math.pow((float)k, s))));
			}
		}
		
		return result;
	}
	

	public double dzetaDouble (double s, int n, boolean fromOne) {
		double result = 0.0;
		
		if(fromOne == true) {
			for(int k = 1; k <= n; k++) {
				result = result + (1/(Math.pow((double)k, s)));
			}
		}
		
		if (fromOne == false) {
			for(int k = n; k > 0; k--) {
				result = result + (1/(Math.pow((double)k, s)));
			}
		}
		
		return result;
		
	}
	
	
	public double etaDouble (double s, int n, boolean fromOne) {
		double result = 0.0;
		if (fromOne == true) {
			for(int k = 1; k <=n; k++) {
				result = result + (Math.pow((double)(-1.0), (double)(k-1)) * (1/(Math.pow((double)k, s))) ); 
			}
		}
		
		if(fromOne == false) {
			for(int k = n; k > 0; k--) {
				result = result + (Math.pow((double)(-1.0), (double)(k-1)) * (1/(Math.pow((double)k, s))) ); 
			}
		}
		return result;
	}
	
	public static void main(String[] args) {
		int [] N = {50, 100, 200, 500, 1000};
		double [] S = {2,3.6667, 5, 7.2, 10};
		SumyCzesciowe x =  new SumyCzesciowe();

		
		for(int i = 0; i < N.length; i++){
			for(int j = 0; j < S.length; j++) {
				System.out.println("-------------------------------------------------------------------");
				System.out.println("N = " + N[i]);
				System.out.println("S = " + S[j]);
				System.out.println("dzetaDouble od przodu: " + x.dzetaDouble(S[j], N[i], true));
				System.out.println("dzetaDouble od tylu:   " + x.dzetaDouble(S[j], N[i], false));
				System.out.println("etaDouble od przodu:   " + x.etaDouble(S[j], N[i], true));
				System.out.println("etaDouble od tylu:     " + x.etaDouble(S[j], N[i], false));
				System.out.println("-------------------------------------------------------------------");
			}
		}
		
		
		

	}

}