package Lab2;

import java.util.Comparator;
import java.util.List;

public class ListsSizeComparator implements Comparator<List>{
	@Override
	public int compare(List list1, List list2) {
		return list2.size() - list1.size();

	}


}
