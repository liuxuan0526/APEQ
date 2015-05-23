package semantic;

import nlp.Nlp;
import edu.stanford.nlp.trees.Tree;

public class Edge {
	public String str;
	public int start;
	public int end;
	public Edge(Tree root,Tree t) {
		str=Nlp.getString(t);
		start=root.leftCharEdge(t);
		end=root.rightCharEdge(t);
	}
	public Edge(Tree root, Tree t1, Tree t2) {
		str=Nlp.getString(t1)+" "+Nlp.getString(t2);
		start=root.leftCharEdge(t1);
		end=root.rightCharEdge(t2);
	}
	public Edge(String ss, int s, int e) {
		str=ss;
		start=s;
		end=e;
	}
	public String getString() {
		return str+"["+start+","+end+"]";
	}
}
