package service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.Iterator;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.document.RawJsonDocument;
import com.couchbase.client.java.document.json.JsonArray;
import com.couchbase.client.java.query.N1qlQuery;
import com.couchbase.client.java.query.N1qlQueryResult;
import com.couchbase.client.java.query.ParameterizedN1qlQuery;
import com.couchbase.client.java.query.SimpleN1qlQuery;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ParseDataService {

    // Initialize the Connection
    public static Bucket bucket = initBucket(); 
	
    private static Bucket initBucket() {
        Cluster cluster = CouchbaseCluster.create("localhost");
        cluster.authenticate("root", "123456");
        Bucket bucket = cluster.openBucket("WaterPoint");
        return bucket;
    }
    
	public static void indexing() throws IOException {
		SimpleN1qlQuery query = N1qlQuery.simple("CREATE PRIMARY INDEX `communities_villages` ON `WaterPoint` USING GSI;");
		bucket.query(query);
	}
    
	public static String parseSource(String myURL) throws IOException {
		String jsonString = callURL(myURL);

	    ObjectMapper mapper = new ObjectMapper();
	    JsonNode jsonMap = mapper.readTree(jsonString);
	    
	    Iterator<JsonNode> jsonArrObj = jsonMap.elements();
	    while (jsonArrObj.hasNext()) {
	        JsonNode jsonObj = jsonArrObj.next();

	        String key = jsonObj.get("_uuid").toString();
	        String subJsonString = jsonObj.toString();
	        RawJsonDocument jsonDoc = RawJsonDocument.create(key, subJsonString);
	        RawJsonDocument data = bucket.upsert(jsonDoc);
	        System.out.println(data);
	    }

	    indexing();
		return "ok";
	}
	
	private static String callURL(String myURL) throws IOException {
		System.out.println("Requeted URL:" + myURL);
		StringBuilder sb = new StringBuilder();
		URLConnection urlConn = null;
		InputStreamReader in = null;
		try {
			URL url = new URL(myURL);
			urlConn = url.openConnection();
			if (urlConn != null)
				urlConn.setReadTimeout(60 * 1000);
			if (urlConn != null && urlConn.getInputStream() != null) {
				in = new InputStreamReader(urlConn.getInputStream(),
						Charset.defaultCharset());
				BufferedReader bufferedReader = new BufferedReader(in);
				if (bufferedReader != null) {
					int cp;
					while ((cp = bufferedReader.read()) != -1) {
						sb.append((char) cp);
					}
					bufferedReader.close();
				}
			}
		in.close();
		} catch (Exception e) {
			throw new RuntimeException("Exception while calling URL:"+ myURL, e);
		} 
		String jsonString = sb.toString();

		return jsonString;
	}
}
