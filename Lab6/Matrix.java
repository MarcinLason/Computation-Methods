import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Matrix {
	private boolean IDF;
	private Dictionary dict;
	private String pathToFiles;
	private Map<Coordinate, Double> matrix;
	private int[] fileswithWord;
	
	public Matrix (String path) {
		this.dict = new Dictionary();
		this.pathToFiles = path;
		this.matrix = new HashMap<Coordinate, Double>();
		this.IDF = false;
	}
	
	public void fillDictionary(String path) {		
		dict.scanFiles(pathToFiles);
		dict.scanWords();
		dict.serializeMapOfWords(path);
	}

	public void fillMatrix() {
		fileswithWord = new int[dict.getAmountOfWords() + 1]; // IDF
		List<File> fileList = dict.getListOfFiles();
		Map<Word, Integer> wordsMap = dict.getMapOfWords();
		
		for(int i = 0; i < fileList.size(); i++) {
			for(String s : dict.getFilesContent().get(fileList.get(i))) {
				Word w = new Word(s);
				
				if(wordsMap.containsKey(w)) {
					
					int j = wordsMap.get(w);
					Coordinate wordPosition  = new Coordinate(j, i);
					
					if(!matrix.containsKey(wordPosition)) {
						matrix.put(wordPosition, 0.0);
						fileswithWord[j]++;
					}
					
					matrix.put(wordPosition, matrix.get(wordPosition) + 1);
				}
			}
		}
	}
	
	public void saveMatrix(String savingPath) {
		 PrintWriter matrixWriter;
			try {
				matrixWriter = new PrintWriter(savingPath);
				
			    for (Map.Entry<Coordinate, Double> entry : matrix.entrySet()) {
			    	if ( IDF ) {
			          matrix.put( entry.getKey(), entry.getValue()*( Math.log( dict.getAmountOfWords() / (fileswithWord[entry.getKey().getX()] + 0.00001) ) ) );
			        }
	
			        matrixWriter.println( entry.getKey().getX() + " " + entry.getKey().getY() + " " + entry.getValue()+" " );
			      }
			      matrixWriter.close();
			} catch (FileNotFoundException e) {
				System.out.println("Wrong path for matrix to save");
			}
	}
	
	public void setIDF(boolean IDFvalue) {
		IDF = IDFvalue;
	}
	
	public Map<Word, Integer> getMapOfWords() {
		return dict.getMapOfWords();
	}
}
