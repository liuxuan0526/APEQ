package entitylinking;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;



public class Miner {
	public static void main(String[] args) throws IOException {
		BufferedReader br=new BufferedReader(new FileReader("mark.txt"));
    	BufferedWriter bs=new BufferedWriter(new FileWriter("new.txt"));
    	while(true) {
    		String str=br.readLine();
    		if(str==null) break;
    		bs.write(str+"\r\n");
    		for(entity e:query(str)) {
    			bs.write(e+"\r\n");
    		}
    		System.out.println(str);
    		System.out.println(query(str));
    	}
		
		bs.close();
	}
	public static String getMinerresult(String o, String str) {
		String ret="";
		for (entity e:query(str)) {
			ret+=e.toString()+"\r\n";
		}
		return ret;
	}
	public static void MinerUpdateloc(entity e, String str, int l, int r) {
		int newl=0,newr=0;
		int k=0;
		for (int i=0;i<str.length();i++) {
			if(l==i) newl=k;
			if(r==i) newr=k;
			if(str.charAt(i)!=' ') ++k;
		}
		e.updateloc(newl, newr);
	}
	public static ArrayList<entity> query(String str) {
		String xml=XMLquery(str);
		ArrayList<entity> ret=new ArrayList<entity>();
		while(true) {
			if(xml.indexOf("detectedTopic ")==-1) break;
			xml=xml.substring(xml.indexOf("detectedTopic "));
			xml=xml.substring(xml.indexOf("title")+7);
			String a=xml.substring(0,xml.indexOf("\""));
			xml=xml.substring(xml.indexOf("\"")+10);
			String bb=xml.substring(0,xml.indexOf("\""));
			double b=Double.valueOf(bb);
			entity tmp=new entity(a,b);
			while(true) {
				if(xml.indexOf("reference ")==-1) break;
				if(xml.indexOf("detectedTopic ")==-1||xml.indexOf("detectedTopic ")>xml.indexOf("reference ")) {
					xml=xml.substring(xml.indexOf("start=")+7);
					String st=xml.substring(0,xml.indexOf("\""));
					xml=xml.substring(xml.indexOf("end=")+5);
					String en=xml.substring(0,xml.indexOf("\""));
					int tmpst=Integer.valueOf(st);
					int tmpen=Integer.valueOf(en);
					MinerUpdateloc(tmp, str, tmpst, tmpen);
				} else break;
			}
			ret.add(tmp);
		}
		return ret;
	}
	public static String XMLquery(String str) {
		String tmp=str.replace(" ", "%20");
		return sendGet("http://wikipedia-miner.cms.waikato.ac.nz/services/wikify","references=true&"
				+ "minProbability=0.25&disambiguationPolicy=loose&source="+tmp);
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
