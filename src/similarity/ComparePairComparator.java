package similarity;

import java.util.Comparator;

public class ComparePairComparator implements Comparator<ComparePair> {
	public final int compare(ComparePair c1, ComparePair c2) {
		double d1=c1.score;
		double d2=c2.score;
		if(d1>d2) return 1;
		else if(d1<d2) return -1;
		else return 0;
	}
}
