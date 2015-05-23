package semantic;

import java.util.ArrayList;
import java.util.List;

import Type.StringAndInt;
import question.Question;

public class Node {
	public List<Description> description;
	public List<Constraint> constraintset;
	//public QueryNode representation=null;
	public Node() {
		description=new ArrayList<Description>();
		constraintset=new ArrayList<Constraint>();
	}
	public void addDescription(Description d) {
		description.add(d);
	}
	public void addConstraint(Constraint c) {
		constraintset.add(c);
	}
	public String getString() {
		String ret="Node: "+this.toString()+"\r\nDescription:\r\n";
		for (Description d:description) {
			ret+="\t"+d.getString()+"\r\n";
		}
		ret+="Constraint:\r\n";
		for (Constraint c:constraintset) {
			ret+="\t"+c.getString()+"\r\n\r\n";
		}
		for (Constraint c:constraintset) {
			ret+=c.node.getString();
		}
		return ret;
	}
	public void POS() {
		List<Description> dtmp=new ArrayList<Description>();
		for (Description d:description) {
			Constraint tmp=d.POS();
			if(tmp!=null) {
				dtmp.add(d);
				constraintset.add(tmp);
			}
		}
		description.removeAll(dtmp);
		for (Constraint tmp:constraintset) {
			tmp.node.POS();
		}
	}
	
}
