import java.io.Serializable;
import java.util.Objects;

public class Word implements Comparable<Word>, Serializable {
	private static final long serialVersionUID = 1L;
	private String text;
	
	public Word (String text) {
		this.text = text;
	}

	 @Override
	public boolean equals(Object w) {
		return this.text.equals(w.toString());
	}
	
	@Override
	public int hashCode() {
	    return Objects.hash(this.text);
	}
	
	 @Override
	public String toString() {
		return text.toString();
	}
	 
	@Override
	public int compareTo(Word word) {
		int result= this.text.compareTo(word.text);
		if (result==0)
			return result;
		else if( this.text.contains(word.toString())||word.toString().contains(this.text))
			return 0;
		else return result;
	}
}