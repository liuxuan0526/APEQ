package semantic;

public class Constraint {
	public Edge edge;
	public Node node;
	public Constraint(Edge e,Node n) {
		edge=e;
		node=n;
	}
	public String getString() {
		if(edge!=null)
			return edge.getString()+"\t"+node.toString();
		else return "null\t"+node.toString();
	}
}
