package semantic;

import java.util.List;

import nlp.Nlp;
import question.Question;
import edu.stanford.nlp.trees.Tree;

public class Procedure {
	public static void process(Tree root, Tree t, Question q, Node x) {
		if(isWHNP(root,t,q)) {
			processWHNP(root,t,q,x);
		} else if(isVP_VBNP(root,t,q)) {
			processVP_VBNP(root,t,q,x);
		} else if(isVP_VBPP(root,t,q)) {
			processVP_VBPP(root,t,q,x);
		} else if(isNP_NPPP(root,t,q)) {
			processNP_NPPP(root,t,q,x);
		} else if(isX_VBNPVP(root,t,q)) {
			processX_VBNPVP(root,t,q,x);
		} else if(isVP_VBNPPP(root,t,q)) {
			processVP_VBNPPP(root,t,q,x);
		} /*else if(isS_NPVP(root,t,q)) {
			processS_NPVP(root,t,q,x);
		}*/ else if(isPP(root,t,q)) {
			processPP(root,t,q,x);
		} else if(isNP(root,t,q)){
			processNP(root,t,q,x);
		} else {
			for (Tree tmp:t.getChildrenAsList()) {
				if(tmp.isPhrasal()) process(root,tmp,q,x);
			}
		}
	}
	public static boolean isS_NPVP(Tree root, Tree t, Question q) {
		List<Tree> u=t.getChildrenAsList();
		return t.isPhrasal()&&t.value().equals("S")&&u.size()==2&&u.get(0).isPhrasal()&&u.get(0).value().equals("NP")&&u.get(1).isPhrasal()&&u.get(1).value().equals("VP");
	}
	public static boolean isVP_VBNPPP(Tree root, Tree t, Question q) {
		List<Tree> u=t.getChildrenAsList();
		return root.leftCharEdge(t)!=0&&t.isPhrasal()&&u.size()==3&&u.get(0).value().startsWith("VB")&&u.get(1).isPhrasal()&&u.get(1).value().equals("NP")&&u.get(2).isPhrasal()&&u.get(2).value().equals("PP");
	}
	public static boolean isPP(Tree root, Tree t, Question q) {
		if(t.isPhrasal()&&t.value().equals("PP")) {
			List<Tree> u=t.getChildrenAsList();
			if(u.size()==2&&u.get(1).isPhrasal()&&u.get(1).value().equals("NP")&&!Nlp.getString(u.get(0)).equals("of")) return true;
		}
		return false;
	}
	public static boolean isNP(Tree root, Tree t, Question q) {
		return t.isPhrasal()&&t.value().equals("NP")&&!Nlp.getString(t).equals("me")&&!Nlp.getString(t).equals("a list")&&NoOther(root,t,q);
	}
	public static boolean NoOther(Tree root, Tree t, Question q) {
		for (Tree tmp:t.getChildrenAsList()) {
			if(tmp.isPhrasal()) {
				if(tmp.value().equals("ADJP")) continue;
				if(!isNP(root,tmp,q)) return false;
			}
		}
		return true;
	}
	public static boolean isWHNP(Tree root, Tree t, Question q) {
		return t.isPhrasal()&&t.value().equals("WHNP"); 
	}
	public static boolean isVP_VBNP(Tree root, Tree t, Question q) {
		List<Tree> u=t.getChildrenAsList();
		return t.isPhrasal()&&t.value().equals("VP")&&root.leftCharEdge(t)!=0&&u.size()==2&&u.get(0).isPreTerminal()&&u.get(0).value().startsWith("VB")&&u.get(1).isPhrasal()&&u.get(1).value().equals("NP");
	}
	public static boolean isVP_VBPP(Tree root, Tree t, Question q) {
		List<Tree> u=t.getChildrenAsList();
		if(t.isPhrasal()&&t.value().equals("VP")&&u.size()==2&&u.get(0).isPreTerminal()&&u.get(0).value().startsWith("VB")&&u.get(1).isPhrasal()&&u.get(1).value().equals("PP")) {
			List<Tree> p=u.get(1).getChildrenAsList();
			if(p.size()==2&&p.get(1).isPhrasal()&&p.get(1).value().equals("NP")) 
				return true;
		}
		return false;
	}
	public static boolean isNP_NPPP(Tree root, Tree t, Question q) {
		List<Tree> u=t.getChildrenAsList();
		if(t.isPhrasal()&&t.value().equals("NP")&&u.size()==2&&u.get(0).isPhrasal()&&u.get(0).value().equals("NP")&&u.get(1).isPhrasal()&&u.get(1).value().equals("PP")) {
			if(Nlp.getString(u.get(0)).equals("a list")) return false;
			else return true;
		}
		return false;
	}
	public static boolean isX_VBNPVP(Tree root, Tree t, Question q) {
		List<Tree> u=t.getChildrenAsList();
		return t.isPhrasal()&&u.size()==3&&u.get(0).isPreTerminal()&&u.get(0).value().startsWith("VB")&&u.get(1).isPhrasal()&&u.get(1).value().equals("NP")&&u.get(2).isPhrasal()&&u.get(2).value().equals("VP");
	}
	public static void processWHNP(Tree root, Tree t, Question q, Node x) {
		String tmp=Nlp.getString(t);
		if(tmp.toLowerCase().startsWith("how many")||tmp.toLowerCase().startsWith("how much")) {
			if(tmp.length()<=9) return;
			else x.addDescription(new Description(tmp.substring(9, tmp.length()),root.leftCharEdge(t)+7,root.rightCharEdge(t)));
		} else if(tmp.indexOf(' ')!=-1) {
			int index=tmp.indexOf(' ');
			x.addDescription(new Description(tmp.substring(index+1, tmp.length()),root.leftCharEdge(t)+index,root.rightCharEdge(t)));
		}
		return;
	}
	public static void processVP_VBNP(Tree root, Tree t, Question q, Node x) {
		List<Tree> u=t.getChildrenAsList();
		Node xx=new Node();
		process(root,u.get(1),q,xx);
		x.addConstraint(new Constraint(new Edge(root,u.get(0)),xx));
		return;
	}
	public static void processVP_VBPP(Tree root, Tree t, Question q, Node x) {
		List<Tree> u=t.getChildrenAsList();
		Node xx=new Node();
		List<Tree> p=u.get(1).getChildrenAsList();
		process(root,p.get(1),q,xx);
		x.addConstraint(new Constraint(new Edge(root, u.get(0),p.get(0)),xx));
	}
	public static void processNP_NPPP(Tree root, Tree t, Question q, Node x) {
		List<Tree> u=t.getChildrenAsList();
		List<Tree> p=u.get(1).getChildrenAsList();
		if(q.cansplit(root.leftCharEdge(t.getChild(1)))&&p.size()==2&&p.get(1).isPhrasal()&&p.get(1).value().equals("NP")) {
			if(Nlp.getString(p.get(0)).equals("of")) {
				Node xx=new Node();
				process(root,p.get(1),q,xx);
				x.addConstraint(new Constraint(new Edge(root,u.get(0)),xx));
			} else {
				x.addDescription(new Description(Nlp.getString(u.get(0)),root.leftCharEdge(u.get(0)),root.rightCharEdge(u.get(0))));
				Node xx=new Node();
				process(root,p.get(1),q,xx);
				//xx.addDescription(new Description(Nlp.getString(p.get(1)),root.leftCharEdge(p.get(1)),root.rightCharEdge(p.get(1))));
				x.addConstraint(new Constraint(new Edge(root,u.get(0)),xx));
			}
		} else {
			x.addDescription(new Description(Nlp.getString(t),root.leftCharEdge(t),root.rightCharEdge(t)));
		}
	}
	public static void processX_VBNPVP(Tree root, Tree t, Question q, Node x) {
		Node xx=new Node();
		process(root,t.getChild(1),q,xx);
		x.addConstraint(new Constraint(new Edge(root,t.getChild(2)),xx));
	}
	public static void processNP(Tree root, Tree t, Question q, Node x) {
		x.addDescription(new Description(Nlp.getString(t),root.leftCharEdge(t),root.rightCharEdge(t)));
	}
	public static void processPP(Tree root, Tree t, Question q, Node x) {
		Node xx=new Node();
		process(root,t.getChild(1),q,xx);
		x.addConstraint(new Constraint(null,xx));
	}
	public static void processVP_VBNPPP(Tree root, Tree t, Question q, Node x) {
		if(!q.cansplit(root.leftCharEdge(t.getChild(2)))) {
			Node xx=new Node();
			xx.addDescription(new Description(Nlp.getString(t.getChild(1))+" "+Nlp.getString(t.getChild(2)),root.leftCharEdge(t.getChild(1)),root.rightCharEdge(t.getChild(2))));
			x.addConstraint(new Constraint(new Edge(root,t.getChild(0)),xx));
		} else {
			Node xx=new Node();
			process(root,t.getChild(1),q,xx);
			String tmp=Nlp.getString(t.getChild(0)).toLowerCase();
			if(tmp.equals("is")||tmp.equals("am")||tmp.equals("are")||tmp.equals("were")||tmp.equals("was")) {
				x.addConstraint(new Constraint(null,xx));
			} else {
				x.addConstraint(new Constraint(new Edge(root,t.getChild(0)),xx));
			}
			if(isPP(root,t.getChild(2),q)) processPP(root,t.getChild(2),q,x);
		}
	}
	public static void processS_NPVP(Tree root, Tree t, Question q, Node x) {
		Node xx=new Node();
		process(root,t.getChild(0),q,xx);
		x.addConstraint(new Constraint(new Edge(root,t.getChild(1)),xx));
	}
}
