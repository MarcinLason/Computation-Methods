import java.util.List;

public class MainClass {

	public static void main(String[] args) {	
		boolean firstUse = false; 
		int amountOfResults = 10; //how many of the best result should be displayed
		int svd = 1; // when 1 - svd enabled, when 0 svd disabled
		int rank = 300; // 
		
		String pathToCatalogWithTextFiles = "Sport";
		String pathToSerializeDictionary = "dictionary.txt";
		String pathToSaveMatrix = "matrix.txt";
		String pythonResultFilePath = "results.txt";
		
		if(firstUse) {
			System.out.println("Your query:");
			for(String text : args) {
				System.out.print(text + " ");
			}
			System.out.println("");
			
			Matrix matrix = new Matrix(pathToCatalogWithTextFiles);
			matrix.setIDF(false);;
			matrix.fillDictionary(pathToSerializeDictionary);
			matrix.fillMatrix();
			matrix.saveMatrix(pathToSaveMatrix);
			
			Query query = new Query(args, matrix.getMapOfWords(), "query.txt");
			query.saveQuery();
			List<String> results = query.computeResults(pythonResultFilePath, amountOfResults, svd, rank);
			System.out.println("Results:");
			System.out.println(results);
			
		}
		
		if (!firstUse) {
			
			System.out.println("Your query:");
			for(String text : args) {
				System.out.print(text + " ");
			}
			System.out.println("");
			
			Dictionary d = new Dictionary();
			d.deserializeMapOfWords(pathToSerializeDictionary);
			
			Query query = new Query(args, d.getMapOfWords(), "query.txt");
			query.saveQuery();
			List<String> results = query.computeResults(pythonResultFilePath, amountOfResults, svd, rank);
			System.out.println("Results:");
			System.out.println(results);
		}
	}
}
