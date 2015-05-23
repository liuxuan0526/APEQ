package entitylinking;

import java.util.ArrayList;

public class entity {
	public String title;
	public double weight;
	public int leftloc;
	public int rightloc;
	public entity(String t, double w) {
		title=t;
		weight=w;
		leftloc=-1;
		rightloc=-1;
	}
	public void updateloc(int l, int r) {
		if(leftloc==-1||l<leftloc) leftloc=l;
		if(rightloc==-1||r>rightloc) rightloc=r;
	}
	public String toString() {
		String str=title+" "+weight+" ["+leftloc+","+rightloc+"]";
		return str;
	}
	
}
