package similarity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

import org.wikibrain.conf.ConfigurationException;
import org.wikibrain.conf.Configurator;
import org.wikibrain.core.cmd.Env;
import org.wikibrain.core.cmd.EnvBuilder;
import org.wikibrain.core.dao.DaoException;
import org.wikibrain.core.dao.LocalPageDao;
import org.wikibrain.core.lang.Language;
import org.wikibrain.core.lang.LocalId;
import org.wikibrain.core.model.LocalPage;
import org.wikibrain.core.model.Title;
import org.wikibrain.phrases.PhraseAnalyzer;
import org.wikibrain.sr.SRMetric;
import org.wikibrain.sr.SRResult;
import org.wikibrain.sr.SRResultList;




public class WikiBrain {
	public static WikiBrain wb=null;
	public SRMetric sr_mw,sr_esa,sr_en;
	public WikiBrain() {
		try {
			Env env = new EnvBuilder().setConfigFile("./lib/wikibrain/customized.conf").setBaseDir("./lib/wikibrain").setMaxThreads(20).build();
		
			Configurator conf = env.getConfigurator();
			 LocalPageDao lpDao = conf.get(LocalPageDao.class);
			 Language simple = Language.getByLangCode("simple");
			
			 sr_en=conf.get(SRMetric.class, "ensemble","language",simple.getLangCode());
			 sr_esa=conf.get(SRMetric.class, "ESA","language",simple.getLangCode());
			// sr_mw=conf.get(SRMetric.class, "milnewitten","language",simple.getLangCode());
		} catch (Exception e) {
			e.printStackTrace();
		}
		 
	}
	public static double ensemble_similarity(String a, String b) {
		try {
			return wb.sr_en.similarity(a, b, false).getScore();
		} catch (Exception e) {
			return 0;
		}
	}
	public static double mw_similarity(String a, String b) {
		try {
			return wb.sr_mw.similarity(a, b, false).getScore();
		} catch (Exception e) {
			return 0;
		}
	}
	public static double esa_similarity(String a, String b) {
		try {
			return wb.sr_esa.similarity(a, b, false).getScore();
		} catch (Exception e) {
			return 0;
		}
	}
	public static void ESA_query(List<QueryPair> list) {
		for (QueryPair tmp:list) {
			tmp.score=esa_similarity(tmp.astr,tmp.bstr);
		}
	}
	public static void ENSEMBLE_query(List<QueryPair> list) {
		for (QueryPair tmp:list) {
			tmp.score=ensemble_similarity(tmp.astr,tmp.bstr);
		}
	}
	public static void main(String[] args) throws IOException {
		wb=new WikiBrain();
		BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
		while(true) {
			String a=br.readLine();
			String b=br.readLine();
			System.out.println("mw:"+mw_similarity(a,b));
			System.out.println("esa:"+esa_similarity(a,b));
			System.out.println("en:"+ensemble_similarity(a,b));
		}
	}

}

