package similarity;

import java.util.Comparator;

public class ComparePair {
	public String str;
	public String predicate;
	public double score;
	public ComparePair(String s, String p) {
		str=s;
		predicate=p;
		score=0.0;
	}
	public void setScore(double d) {
		score=d;
	}
}
