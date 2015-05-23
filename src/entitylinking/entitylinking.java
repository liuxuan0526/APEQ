package entitylinking;

import java.util.ArrayList;
import java.util.List;

public class entitylinking {
	public static int method=0;
	public static void setmethod(int m) {
		method=m;
	}
	public static List<entity> entitylink(String str) {
		if(method==0) {
			List<entity> ret=Miner.query(str);
			List<entity> rem=new ArrayList<entity>();
			for (entity e:ret) {
				if(e.title.startsWith("List of")||(e.title.charAt(0)>='a'&&e.title.charAt(0)<='z')) rem.add(e);
			}
			ret.removeAll(rem);
			for (entity e:ret) {
				e.title="http://dbpedia.org/resource/"+e.title.replace(" ", "_");
				String tmp=dbpedia.DBGraph.getEntityRedirectUri(e.title);
				if(tmp!=null) e.title=tmp;
			}
			return ret;
		}
		else return null;
	}
	public static void main(String[] args) {
		String str="Who is the mayor of Rotterdam?";
		for (entity e:entitylink(str)) {
			System.out.println(e);
		}
	}
}
