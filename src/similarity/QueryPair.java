package similarity;

public class QueryPair {
	public String astr;
	public String bstr;
	public double score;
	public QueryPair(String a, String b) {
		astr=a;
		bstr=b;
	}
	public void setScore(double d) {
		score=d;
	}
}
