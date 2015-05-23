package similarity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import dbpedia.DBGraph;


public class Similarity {
	public enum Method {
		ESA, UMBC, ENSEMBLE
	}
	public enum Approach {
		MAX, MEAN
	}
	public static void getScore(List<ComparePair> list, Method method, Approach approach) {
		HashMap<String, String> predicateTolabel=new HashMap<String, String>();
		HashMap<String, HashMap<String,QueryPair>> map=new HashMap<String, HashMap<String,QueryPair>>();
		List<QueryPair> queryList=prepare(list,predicateTolabel,map);
		simiquery(queryList,method);
		calcScore(list,predicateTolabel,map,approach);
	}
	public static void calcScore(List<ComparePair> list, HashMap<String, String> predicateTolabel, HashMap<String, HashMap<String,QueryPair>> map, Approach approach) {
		for (ComparePair c:list) {
			if(c.score<-0.000001) continue;
			String label=predicateTolabel.get(c.predicate);
			if(label==null) {
				c.score=-1.0;
				continue;
			} else {
				c.score=calc(label.split(" "),c.str.split(" "),map,approach);
			}
		}
	}
	public static double calc(String[] a, String[] b, HashMap<String, HashMap<String,QueryPair>> map, Approach approach) {
		double ans=0.0;
		if(approach==Approach.MAX) {
			for (String tmpa:a) {
				for (String tmpb:b) {
					double tmp=find(map,tmpa,tmpb);
					if(tmp>ans) ans=tmp;
				}
			}
			return ans;
		} else if(approach==Approach.MEAN) {
			if(a.length==0||b.length==0) return 0.0;
			double[] as=new double[a.length];
			double[] bs=new double[b.length];
			for (int i=0;i<a.length;i++) as[i]=0.0;
			for (int i=0;i<b.length;i++) bs[i]=0.0;
			for (int i=0;i<as.length;i++) {
				for (int j=0;j<as.length;j++) {
					double tmp=find(map,a[i],b[j]);
					if(tmp>as[i]) as[i]=tmp;
					if(tmp>bs[j]) bs[j]=tmp;
				}
			}
			double ret=0.0;
			double tmp=0.0;
			for (int i=0;i<as.length;i++) tmp+=as[i];
			ret+=tmp/(2*as.length);
			tmp=0.0;
			for (int i=0;i<bs.length;i++) tmp+=bs[i];
			ret+=tmp/(2*bs.length);
			return ret;
		}
		return 0.0;
	}
	public static double find(HashMap<String,HashMap<String,QueryPair>> map, String a, String b) {
		if(a.compareTo(b)>0) {
			String tmp=a;
			a=b;
			b=tmp;
		}
		HashMap<String,QueryPair> tmpmap=map.get(a);
		if(tmpmap==null) return 0.0;
		QueryPair tmp=tmpmap.get(b);
		if(tmp==null) return 0.0;
		return tmp.score;
	}
	public static List<QueryPair> prepare(List<ComparePair> list, HashMap<String, String> predicateTolabel, HashMap<String, HashMap<String,QueryPair>> map) {
		List<QueryPair> ret=new ArrayList<QueryPair>();
		for (ComparePair c:list) {
			String label=predicateTolabel.get(c.predicate);
			if(label==null) {
				label=DBGraph.getLabel(c.predicate);
				if(label==null) {
					c.score=-1.0;
					continue;
				}
				predicateTolabel.put(c.predicate, label);
			}
			for (String a:c.str.split(" ")) {
				for (String b:label.split(" ")) {
					if(a.compareTo(b)>0) {
						String tmp=a;
						a=b;
						b=tmp;
					}
					QueryPair qp=setQueryPair(map,a,b);
					if(qp!=null) ret.add(qp);
				}
			}	
		}
		return ret;
	}
	public static void simiquery(List<QueryPair> queryList, Method method) {
		if(method==Method.UMBC) {
			UMBC.UMBC_query(queryList);
		} else if(method==Method.ESA) {
			WikiBrain.ESA_query(queryList);
		} else if(method==Method.ENSEMBLE) {
			WikiBrain.ENSEMBLE_query(queryList);
		}
	}
	public static QueryPair setQueryPair(HashMap<String,HashMap<String,QueryPair>> map, String a, String b) {
		HashMap<String,QueryPair> tmpmap=map.get(a);
		if(tmpmap==null) {
			tmpmap=new HashMap<String,QueryPair>();
			QueryPair tmp=new QueryPair(a,b);
			tmpmap.put(b, tmp);
			map.put(a, tmpmap);
			return tmp;
		} else {
			QueryPair tmp=tmpmap.get(b);
			if(tmp==null) {
				tmp=new QueryPair(a,b);
				tmpmap.put(b, tmp);
				return tmp;
			} else return null;
		}
	}
	public void getSortedScore(List<ComparePair> list, Method method, Approach approach) {
		getScore(list,method,approach);
		list.sort(new ComparePairComparator());
	}
	
}
