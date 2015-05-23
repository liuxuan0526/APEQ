package question;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import nlp.Nlp;
import edu.stanford.nlp.trees.Tree;
import entitylinking.entity;
import entitylinking.entitylinking;


public class Question {
	public String question;
	public List<entity> candidate;
	public Question(String str) {
		question=str;
		candidate=entitylinking.entitylink(question);
	}
	public boolean cansplit(int loc) {
		for (entity e: candidate) {
			if(loc>e.leftloc&&loc<e.rightloc) {
				if(has(loc,e.rightloc)) return true;
				else return false;
			}
		}
		return true;
	}
	public boolean has(int l, int r) {
		for (entity e: candidate) {
			if(e.leftloc>l&&e.rightloc<=r) return true;
		}
		return false;
	}
	public entity getfirstentity(int left, int right) {
		double confidence=0;
		int size=0;
		entity et=null;
		for (entity e: candidate) {
			if(e.leftloc>=left&&e.rightloc<=right) {
				if(e.rightloc-e.leftloc>size||(e.rightloc-e.leftloc==size&&e.weight>confidence)) {
					size=e.rightloc-e.leftloc;
					confidence=e.weight;
					et=e;
				}
			}
		}
		return et;
	}
	public String getTerm(int l, int r) {
		String ret="";
		int k=0;
		for (int i=0;i<question.length();i++) {
			if(k==l) {
				if(question.charAt(i)==' ') continue;
				else {
					ret+=question.charAt(i);
					++k;
				}
			} else if(k>l&&k<r) {
				ret+=question.charAt(i);
				if(question.charAt(i)!=' ') ++k;
			} else {
				if(question.charAt(i)!=' ') ++k;
			}
		}
		return ret;
	}
	public static boolean isInside(entity e1, entity e2) {
		if(e1.leftloc==e2.leftloc&&e1.rightloc==e2.rightloc) return false;
		if(e1.leftloc>=e2.leftloc&&e1.rightloc<=e2.rightloc) return true;
		return false;
	}
	public List<entity> getRangeSetOnce(int start, int end) {
		List<entity> l=new ArrayList<entity>();
		for (int i=0;i<candidate.size();i++) {
			if(!(candidate.get(i).leftloc>=start&&candidate.get(i).rightloc<=end)) continue;
			else l.add(candidate.get(i));
		}
		List<entity> ans=new ArrayList<entity>();
		for (int i=0;i<l.size();i++) {
			boolean flag=true;
			for (int j=0;j<l.size();j++) {
				if(i==j) continue;
				if(isInside(l.get(i),l.get(j))) {
					flag=false;
					break;
				}
			}
			if(flag) {
				ans.add(l.get(i));
			}
		}
		return ans;
	}
	public List<List<entity>> getRangeSet(int start, int end) {
		List<List<entity>> l=new ArrayList<List<entity>>();
		for (entity e:candidate) {
			int length=l.size();
			if(!(e.leftloc>=start&&e.rightloc<=end)) continue;
			for (int i=0;i<length;i++) {
				List<entity> tmp=l.get(i);
				if(!intersect(tmp,e)) {
					List<entity> tt=new ArrayList<entity>(tmp);
					tt.add(e);
					l.add(tt);
				}
			}
			List<entity> tt=new ArrayList<entity>();
			tt.add(e);
			l.add(tt);
		}
		List<List<entity>> ans=new ArrayList<List<entity>> ();
		for (int i=0;i<l.size();i++) {
			boolean flag=true;
			for (int j=0;j<l.size();j++) {
				if(i==j) continue;
				if(l.get(j).containsAll(l.get(i))) {
					flag=false;
					break;
				}
			}
			if(flag) ans.add(l.get(i));
		}
		System.out.println("size: "+ans.size());
		return ans;
	}
	public boolean intersect(List<entity> list, entity e) {
		for (entity e1:list) {
			if(intersect(e1,e)) return true;
		}
		return false;
	}
	public boolean intersect(entity e1, entity e2) {
		if(e1.leftloc>=e2.rightloc||e2.leftloc>=e2.rightloc) return false;
		else return true;
	}
}
