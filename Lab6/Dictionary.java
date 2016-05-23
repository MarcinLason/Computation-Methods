import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.tartarus.snowball.ext.englishStemmer;


public class Dictionary {
	private Map <Word, Integer> mapOfWords; //main structure, final dictionary
	private Map<File,ArrayList<String>> filesContent;
	private List<File> listOfFiles;
	
	public Dictionary () {
		this.listOfFiles = new ArrayList<File>();
		this.mapOfWords = new TreeMap<Word, Integer>();
		this.filesContent = new HashMap<File, ArrayList<String>>();
	}

	public void scanFiles (String path) {
		boolean containOnlyRegularFiles = true;
		File folder = new File(path); 
		
		if(folder.isDirectory() == false) {
			System.out.println("Error! Wrong  path to the directory with texts!");
			return;
		}
		
		for(File f : folder.listFiles()) { //adding all files from the catalog to the list
			if(f.isFile()) {
				listOfFiles.add(f);
			}
			else {
				containOnlyRegularFiles = false;
			}
		}
		
		if(!containOnlyRegularFiles) {
			System.out.println("Your catalog contains other catalogs or files which can't be read");
		}
	}
	
	public void scanWords() {
		int wordsCounter = 0;
		if (getAmountOfFiles() == 0) {
			System.out.println("Firstly you have to scan files.");
			return;
		}
		
		for(File file: listOfFiles) {
			try {
				String fileContentInString = new String(Files.readAllBytes(Paths.get(file.getPath()))).replaceAll("['\",.*()&^%$#!?></\\{}_|~\\+=;:\\[\\]-]", " ");
				List<String> wordsFromFile = stem(fileContentInString.toLowerCase().split("\\s"));
				filesContent.put(file, new ArrayList<String>(wordsFromFile));
				
				for(String w : wordsFromFile) {
					Word word = new Word(w);
					
					if(!mapOfWords.containsKey(word)) {
						mapOfWords.put(word, wordsCounter);
						wordsCounter++;
					}
				}
			}catch(IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void serializeMapOfWords(String path) {
		try {
			FileOutputStream fos = new FileOutputStream(path);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(mapOfWords);
			oos.close();
			fos.close();
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void deserializeMapOfWords(String path) {
		mapOfWords = null;
		try {
			FileInputStream fis = new FileInputStream(path);
			ObjectInputStream ois = new ObjectInputStream(fis);
			mapOfWords = (TreeMap<Word, Integer>) ois.readObject();
			ois.close();
			fis.close();
		}catch (IOException e) {
			e.printStackTrace();
			return;
		} catch(ClassNotFoundException c) {
			c.printStackTrace();
			return;
		}
	}
	
	public static List<String> stem (String[] input) {
		englishStemmer st = new englishStemmer();
		List<String> result = new ArrayList<>();
		
		for(String word : input) {
			st.setCurrent(word);
			if(st.stem()) {
				result.add(st.getCurrent());
			}
		}
		return result;
	}
	
	public List<File> getListOfFiles() {
		return listOfFiles;
	}

	public int getAmountOfFiles() {
		return listOfFiles.size();
	}
	
	public int getAmountOfWords() {
		return mapOfWords.size();
	}
	
	public Map<File,ArrayList<String>> getFilesContent () {
		return filesContent;
	}
	
	public Map<Word, Integer> getMapOfWords() {
		return mapOfWords;
	}	
}