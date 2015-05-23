package similarity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

public class UMBCTask implements Callable<String> {
	private List<QueryPair> ins;
	public UMBCTask(List<QueryPair> in) {
		ins=in;
	}
	public String call() {
		for (QueryPair sp:ins) {
			sp.score=UMBCSimilarity(sp.astr,sp.bstr);
		}
		return "";
	}
	public static double UMBCSimilarity(String a, String b) {
		String aa=a.replace(" ", "%20");
		String bb=b.replace(" ", "%20");
		String ret=sendGet("http://swoogle.umbc.edu/SimService/GetSimilarity","operation=api&phrase1="+aa+"&phrase2="+bb);
		Double d=Double.parseDouble(ret);
		if(d<0.0) return 0.0;
		else return d;
	}
	public static String sendGet(String url, String param) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url + "?" + param;
            URL realUrl = new URL(urlNameString);
            URLConnection connection = realUrl.openConnection();
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            connection.connect();
            Map<String, List<String>> map = connection.getHeaderFields();
           /* for (String key : map.keySet()) {
                System.out.println(key + "--->" + map.get(key));
            }*/
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }
}
