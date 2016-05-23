import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Query {
	private String[] wordsToFind;
	private Map<Word, Integer> mapOfWords;
	private String savedQueryPath;
	
	public Query(String[] words, Map<Word, Integer> mapOfWords, String path) {
		this.wordsToFind = words;
		this.mapOfWords = mapOfWords;
		this.savedQueryPath = path;
	}
	
	public void saveQuery() {
		int amountOfWords = mapOfWords.size();
		int [] query = new int[amountOfWords];
		
		if(wordsToFind.length < 1) {
			System.out.println("Your query have to contains at least one word!");
		}
		
		ArrayList<String> stemmed = new ArrayList<String>(Dictionary.stem(wordsToFind));
		
		for(String s : stemmed) {
			Word w = new Word(s.toLowerCase());
			if(mapOfWords.containsKey(w)) {
				query[mapOfWords.get(w)]++;
			}
		}
		
		try {
			PrintWriter pr = new PrintWriter(savedQueryPath);
			for ( int elem : query) {
				pr.println(elem);
			}
			pr.close();
		} catch (FileNotFoundException e) {
			System.out.println("Wrong path to save words from your query.");
		}		
	}
	
	public List<String> computeResults(String pythonResultFilePath, int amountOfResults, int svd, int rank) {
		Dictionary loadedDict = new Dictionary();
		loadedDict.deserializeMapOfWords("dictionary.txt");
		loadedDict.scanFiles("Sport");
		
		if (rank > loadedDict.getAmountOfFiles()) {
			System.out.println("Rank cannot be higher than amount of files!");
		}
		
		runPython(loadedDict.getAmountOfWords(), loadedDict.getAmountOfFiles(), amountOfResults, svd, rank);
		
		List<String> result = new ArrayList<String>();
		List<File> listOfFiles = loadedDict.getListOfFiles();
		String contents[];
		
		try {
			contents = new String(Files.readAllBytes(Paths.get(pythonResultFilePath))).split(" ");
			int fileNumber;
			
			for(String s : contents) {
				fileNumber = Integer.parseInt(s);
				result.add(listOfFiles.get(fileNumber).getName());
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Error while reading Python results");
		}
		return result;
	}
	
	private void runPython(int amountOfWords, int amountOfFiles, int amountOfResults, int svd, int rank) {
		String [] command = { "python3", "browser.py", Integer.toString(amountOfWords), Integer.toString(amountOfFiles),Integer.toString(amountOfResults), Integer.toString(svd),Integer.toString(rank)}; 
		
		ProcessBuilder pb = new ProcessBuilder(command); 
		Process p;		
		try {
			p = pb.start();
			p.waitFor();
		} catch (IOException e) {
			System.out.println("Error while launching python");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
