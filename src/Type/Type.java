package Type;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import Type.WordNet;
import nlp.Nlp;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.util.CoreMap;

public class Type {
	public static Type type=null;
	List<String> ontolist=new ArrayList<String>();
	List<String> labellist=new ArrayList<String>();
	public Type() {
		try {
			BufferedReader br=new BufferedReader(new FileReader("./dict/TypeWithLabel.txt"));
			while(true) {
				String str=br.readLine();
				if(str==null) break;
				String label=br.readLine();
				ontolist.add(str);
				labellist.add(label);
			}
			br.close();
		} catch (Exception e){
			e.printStackTrace();
		}
		
	}
	public static String lemma(String str) {
		Annotation document = Nlp.tool.pipeline.process(str);  
		CoreMap sentence=document.get(SentencesAnnotation.class).get(0);
		List<CoreLabel> mid=sentence.get(TokensAnnotation.class);
		String ans="";
		for(int i=0;i<mid.size();i++)
		{       
			CoreLabel token=mid.get(i);
			//String word = token.get(TextAnnotation.class);      
			String lemma = token.get(LemmaAnnotation.class);
			ans+=lemma;
			if(i!=mid.size()-1) ans+=" ";
		}
		return ans;
	}
	public static String getType(String str) {
		String[] tmp=str.split(" ");
		String string="";
		for (int i=0;i<tmp.length;i++) {
			string+=lemma(tmp[i]);
		}
		List<String> ret=new ArrayList<String>();
		for (int i=0;i<type.labellist.size();i++) {
			String atmp="";
			tmp=type.labellist.get(i).split(" ");
			for (String t:tmp) atmp+=t;
			if(string.toLowerCase().equals(atmp.toLowerCase())) {
				return type.ontolist.get(i);
			}
		}
		if(str.split(" ").length!=1) return null;
		for (int i=0;i<type.labellist.size();i++) {
			if(WordNet.isSynset(lemma(str.split(" ")[0]), type.labellist.get(i))) {
				return type.ontolist.get(i);
			}
		}
		
		return null;
	}
	public static StringAndInt getTypeFromFocus(String str) {
		while(true) {
			String tmp=getType(str);
			if(tmp!=null) {
				return new StringAndInt("<"+tmp+">" , str.replace(" ", "").length());
			}
			int index=str.indexOf(' ');
			if(index==-1) break;
			str=str.substring(index+1,str.length());
		}
		
		return null;
	}
	public static void main(String[] args) {
		System.out.println(getTypeFromFocus("programming language").str);
	}
}
