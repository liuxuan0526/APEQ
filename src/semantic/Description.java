package semantic;

public class Description {
	public String str;
	public int start;
	public int end;
	public Description(String ss, int s, int e) {
		str=ss;
		start=s;
		end=e;
	}
	public String getString() {
		return str+"["+start+","+end+"]";
	}
	public Constraint POS() {
		if(str.contains("'s")) {
			int tmp=str.indexOf("'s");
			if(tmp==str.length()-2) return null;
			else {
				String str1=str.substring(0, tmp-1);
				String str2=str.substring(tmp+3);
				int k=0;
				for (int i=0;i<str1.length();i++) if(str1.charAt(i)!=' ') ++k;
				Node x=new Node();
				x.addDescription(new Description(str1,start,start+k));
				return new Constraint(new Edge(str2,start+k+2,end),x);
			}
		} else {
			return null;
		}
	}
}
