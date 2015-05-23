package dbpedia;

import java.util.ArrayList;
import java.util.List;

import com.franz.agraph.jena.AGModel;
import com.franz.agraph.jena.AGQuery;
import com.franz.agraph.jena.AGQueryExecutionFactory;
import com.franz.agraph.jena.AGQueryFactory;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.RDFNode;

public class DBGraph {
	public static String getEntityRedirectUri(String s) {
		String queryString = "SELECT ?o WHERE {"
				+ "<"+s+"> <http://dbpedia.org/ontology/wikiPageRedirects> ?o."
				+ "}";
		AGModel model=null;
		try {
			model = ClientManagement.getAgModel(false);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		AGQuery sparql = AGQueryFactory.create(queryString);
		QueryExecution qe = AGQueryExecutionFactory.create(sparql, model);
		
		try {
			ResultSet results = qe.execSelect();
			while (results.hasNext()) {
				QuerySolution result = results.next();
		
				RDFNode o = result.get("o");
				return o.toString();
			}
		} finally {
			qe.close();
		}
		return null;
	}
	public static String getLabel(String p) {
		String queryString = "SELECT ?o WHERE {"
				+ "<"+p+"> rdfs:label ?o."
				+ "}";
		AGModel model=null;
		try {
			model = ClientManagement.getAgModel(false);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		AGQuery sparql = AGQueryFactory.create(queryString);
		QueryExecution qe = AGQueryExecutionFactory.create(sparql, model);
		
		try {
			ResultSet results = qe.execSelect();
			while (results.hasNext()) {
				QuerySolution result = results.next();
		
				RDFNode o = result.get("o");
				String tmpans=o.toString();
				if(tmpans.endsWith("@en")) {
					return tmpans.substring(0, tmpans.length()-3);
				}
			}
		} finally {
			qe.close();
		}
		return null;
	}
	/*
	public static List<List<String>> query(String queryString, queryStruct qs) {
		AGModel model=null;
		try {
			model = ClientManagement.getAgModel(false);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		AGQuery sparql = AGQueryFactory.create(queryString);
		QueryExecution qe = AGQueryExecutionFactory.create(sparql, model);
		
		double score=0.0;
		List<String> tmpans;
		List<List<String>> ans=new ArrayList<List<String>>();
		try {
			ResultSet results = qe.execSelect();
			while (results.hasNext()) {
				QuerySolution result = results.next();
				
				tmpans=new ArrayList<String>();
				for (int i=0;i<qs.compare.size();i++) {
					RDFNode o = result.get(qs.compare.get(i).labelindex.replace("?", ""));
					tmpans.add(o.toString());
				}
				ans.add(tmpans);
			}
		} finally {
			qe.close();
		}
		return ans;
	}
	*/
	public static List<String> srcquery(String queryString, String srclabel) {
		AGModel model=null;
		try {
			model = ClientManagement.getAgModel(false);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		AGQuery sparql = AGQueryFactory.create(queryString);
		QueryExecution qe = AGQueryExecutionFactory.create(sparql, model);
		
		List<String> ret=new ArrayList<String>();
		try {
			ResultSet results = qe.execSelect();
			while (results.hasNext()) {
				QuerySolution result = results.next();
				RDFNode o=result.get(srclabel);
				if(o.isLiteral()) {
					ret.add(o.asLiteral().getLexicalForm());
				} else {
					ret.add(o.toString());
				}
			}
		} finally {
			qe.close();
		}
		return ret;
	}
	public static void main(String[] args) {
		String str="Select ?s1 Where { { ?s1 <http://dbpedia.org/ontology/completionDate> <http://dbpedia.org/resource/Titanic_(1953_film)> . } UNION { <http://dbpedia.org/resource/Titanic_(1953_film)> <http://dbpedia.org/ontology/completionDate> ?s1. }  UNION { ?s1 <http://dbpedia.org/ontology/completionDate> <http://dbpedia.org/resource/Titanic_(1943_film)> . } UNION { <http://dbpedia.org/resource/Titanic_(1943_film)> <http://dbpedia.org/ontology/completionDate> ?s1. }  UNION { ?s1 <http://dbpedia.org/ontology/completionDate> <http://dbpedia.org/resource/Titanic_(musical)> . } UNION { <http://dbpedia.org/resource/Titanic_(musical)> <http://dbpedia.org/ontology/completionDate> ?s1. }  UNION { ?s1 <http://dbpedia.org/ontology/completionDate> <http://dbpedia.org/resource/Titanic_(1997_film)> . } UNION { <http://dbpedia.org/resource/Titanic_(1997_film)> <http://dbpedia.org/ontology/completionDate> ?s1. }  UNION { ?s1 <http://dbpedia.org/ontology/completionDate> <http://dbpedia.org/resource/RMS_Titanic> . } UNION { <http://dbpedia.org/resource/RMS_Titanic> <http://dbpedia.org/ontology/completionDate> ?s1. }  UNION { ?s1 <http://dbpedia.org/ontology/completionDate> <http://dbpedia.org/resource/Titanic_(disambiguation)> . } UNION { <http://dbpedia.org/resource/Titanic_(disambiguation)> <http://dbpedia.org/ontology/completionDate> ?s1. }  FILTER(datatype(?s1)=xsd:dateTime||datatype(?s1)=xsd:date||datatype(?s1)=xsd:time||datatype(?s1)=xsd:gYearMonth||datatype(?s1)=xsd:gYear||datatype(?s1)=xsd:gMonthDay||datatype(?s1)=xsd:gDay||datatype(?s1)=xsd:gMonth) } ";
		
		for (String s:srcquery(str,"?s1")) {
			System.out.println(s);
		}
	}
}
