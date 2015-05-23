package similarity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class UMBC {
	public static int threadnum=50;
	public static void UMBC_query(List<QueryPair> l) {
		int tmp=l.size();
		if(tmp>threadnum) tmp=threadnum; 
		ExecutorService exec=Executors.newCachedThreadPool();
		ArrayList<List<Double>> s=new ArrayList<List<Double>>();
		ArrayList<Future<String>> results=new ArrayList<Future<String>>();
		for (int i=0;i<tmp;i++) {
			List<QueryPair> ins=new ArrayList<QueryPair>();
			int j=i;
			while(j<l.size()) {
				ins.add(l.get(j));
				j+=tmp;
			}
			results.add(exec.submit(new UMBCTask(ins)));
		}
		exec.shutdown();
		for (Future<String> fs:results) {
			try {
				fs.get();
			} catch (InterruptedException e) {
				return;
			} catch(ExecutionException e) {
				e.printStackTrace();
			} finally {
				exec.shutdown();
			}
		}
		return;
	}
}
