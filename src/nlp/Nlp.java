package nlp;

import java.io.StringReader;
import java.util.List;
import java.util.Properties;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.parser.common.ParserQuery;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.process.Tokenizer;
import edu.stanford.nlp.process.TokenizerFactory;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.util.ScoredObject;

public class Nlp {
	LexicalizedParser lp;
	ParserQuery lpq ;
	public StanfordCoreNLP pipeline;
	public static Nlp tool=null;
	public Nlp() {
		String parserModel = "edu/stanford/nlp/models/lexparser/englishRNN.ser.gz";
		lp = LexicalizedParser.loadModel(parserModel);
		lpq=lp.parserQuery();
		Properties props = new Properties(); 
        props.put("annotators", "tokenize, ssplit, pos, lemma"); 
        pipeline = new StanfordCoreNLP(props, false);
	}
	public static Tree getParseTree(String sentence) {
		TokenizerFactory<CoreLabel> tokenizerFactory =
		        PTBTokenizer.factory(new CoreLabelTokenFactory(), "");
		Tokenizer<CoreLabel> tok =
		        tokenizerFactory.getTokenizer(new StringReader(sentence));
		List<CoreLabel> Words = tok.tokenize();
		Tree tree=tool.lp.apply(Words);
		return tree;
	}
	public static List<ScoredObject<Tree>> getkBestParseTree(String sentence, int size) {
		TokenizerFactory<CoreLabel> tokenizerFactory =
		        PTBTokenizer.factory(new CoreLabelTokenFactory(), "");
		Tokenizer<CoreLabel> tok =
		        tokenizerFactory.getTokenizer(new StringReader(sentence));
		List<CoreLabel> Words = tok.tokenize();
		tool.lpq.parse(Words);
		List<ScoredObject<Tree>> kBest = tool.lpq.getKBestPCFGParses(size);
		return kBest;
	}
	private static String _getString(Tree tree) {
		if(tree.isLeaf()) return tree.value()+" ";
		String ret="";
		for (Tree t:tree.getChildrenAsList()) ret+=_getString(t);
		return ret;
	}
	public static String getString(Tree tree) {
		String ret=_getString(tree);
		if(ret!="") return ret.substring(0, ret.length()-1);
		else return "";
	}
	/*public static String origin(String text) {
		Annotation document = tool.pipeline.process(text);  
		CoreMap sentence=document.get(SentencesAnnotation.class).get(0);
		List<CoreLabel> mid=sentence.get(TokensAnnotation.class);
		String[] ans=new String[mid.size()];
		for(int i=0;i<mid.size();i++)
		{       
			CoreLabel token=mid.get(i);
			//String word = token.get(TextAnnotation.class);      
			String lemma = token.get(LemmaAnnotation.class);
			ans[i]=lemma;
		}
        return ans;
	}*/
}
