package question;

import java.util.ArrayList;
import java.util.List;





import dbpedia.ClientManagement;
import similarity.WikiBrain;
import nlp.Nlp;
import Type.Type;
import Type.WordNet;
import edu.stanford.nlp.trees.Tree;

public class process {
	public static void init() {
		Nlp.tool=new Nlp();
		WikiBrain.wb=new WikiBrain();
		WordNet.wordnet=new WordNet();
		Type.type=new Type();
		try {
			ClientManagement.getAgModel(false);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
