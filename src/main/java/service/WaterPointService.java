package service;

import java.io.IOException;
import java.util.Arrays;

import org.springframework.stereotype.Service;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.document.json.JsonArray;
import com.couchbase.client.java.query.N1qlQuery;
import com.couchbase.client.java.query.N1qlQueryResult;
import com.couchbase.client.java.query.N1qlQueryRow;
import com.couchbase.client.java.query.ParameterizedN1qlQuery;

import model.Community;
import model.CommunityRank;
import model.StatisticModel;
import model.WaterPoint;

@Service
public class WaterPointService {
	
    // Initialize the Connection
    public static Bucket bucket = initBucket(); 
	
    private static Bucket initBucket() {
        Cluster cluster = CouchbaseCluster.create("localhost");
        cluster.authenticate("root", "123456");
        Bucket bucket = cluster.openBucket("WaterPoint");
        return bucket;
    }

	public static StatisticModel stat() throws IOException {
		N1qlQueryResult wp = waterPoint("yes");
		String total = wp.allRows().get(0).value().get("waterPointTotal").toString();
		String numberNotWorking = wp.allRows().get(0).value().get("waterFunctioningNo").toString();
		String numberWorking = wp.allRows().get(0).value().get("waterFunctioningYes").toString();
		String percentWorking = wp.allRows().get(0).value().get("percent").toString();
		
		StatisticModel waterPointModel = new StatisticModel();
		
		WaterPoint waterPoint = new WaterPoint();
		waterPoint.setTotal(total);
		waterPoint.setPercentWorking(percentWorking);
		waterPoint.setNumberWorking(numberWorking);
		waterPoint.setNumberNotWorking(numberNotWorking);
		waterPointModel.setWaterPoint(waterPoint);
		
		N1qlQueryResult comm = wpPerCommunity("yes", "total", "waterFunctioningYes");
		for (N1qlQueryRow row : comm) {

			Community community = new Community();
			community.setTotal(row.value().get("total").toString());
			community.setPercentWorking(row.value().get("percent").toString());
			community.setNumberWorking(row.value().get("waterFunctioningYes").toString());
			community.setNumberNotWorking(row.value().get("waterFunctioningNo").toString());
			waterPointModel.setCommunity(row.value().get("communities_villages").toString(), community);
			
		}

		N1qlQueryResult commRG = wpPerCommunity("yes", "percent", "waterFunctioningYes");
		for (N1qlQueryRow row : commRG) {

			CommunityRank communityRank = new CommunityRank();
			communityRank.setPercent(row.value().get("percent").toString());
			waterPointModel.setCommunityRankGood(row.value().get("communities_villages").toString(), communityRank);
			
		}
		
		N1qlQueryResult commRB = wpPerCommunity("no", "percent", "waterFunctioningNo");
		for (N1qlQueryRow row : commRB) {

			CommunityRank communityRank = new CommunityRank();
			communityRank.setPercent(row.value().get("percent").toString());
			waterPointModel.setCommunityRankBad(row.value().get("communities_villages").toString(), communityRank);
			
		}
		
//        System.out.println(waterPointModel);

		return waterPointModel;
		
	}
	
	public static N1qlQueryResult waterPoint(String val) throws IOException {
		ParameterizedN1qlQuery query = N1qlQuery.parameterized("SELECT  \n" + 
				"Sum( Case When WP.water_functioning=\"no\" Then 1 Else 0 End ) as waterFunctioningNo,\n" + 
				"Sum( Case When WP.water_functioning=\"yes\" Then 1 Else 0 End ) as waterFunctioningYes,\n" + 
				"Count( WP.water_functioning ) as waterPointTotal,\n" + 
				"Sum( Case When WP.water_functioning=$1 Then 1 Else 0 End )/count(WP.water_functioning)*100 as percent\n" + 
				"FROM WaterPoint WP",
        JsonArray.from(val));
		
		N1qlQueryResult result = bucket.query(query);

		return result;
	}
	
	public static N1qlQueryResult wpPerCommunity(String val, String sort, String sort2) throws IOException {

		String[] allow = new String[] {"total","percent"};
		if(!Arrays.asList(allow).contains(sort)) {
			sort = "total";
		}
		String[] allow2 = new String[] {"waterFunctioningYes","waterFunctioningNo"};
		if(!Arrays.asList(allow2).contains(sort2)) {
			sort = "waterFunctioningYes";
		}
		ParameterizedN1qlQuery query = N1qlQuery.parameterized("SELECT communities_villages, \n" + 
        		"Sum( Case When WP.water_functioning=\"no\" Then 1 Else 0 End ) as waterFunctioningNo,\n" + 
        		"Sum( Case When WP.water_functioning=\"yes\" Then 1 Else 0 End ) as waterFunctioningYes,\n" +
        		"Count( WP.water_functioning ) as total,\n" +
        		"Sum( Case When WP.water_functioning=$1 Then 1 Else 0 End )/count(WP.water_functioning)*100 as percent\n" + 
        		"FROM WaterPoint WP \n" + 
        		"GROUP BY  WP.communities_villages \n" + 
        		"ORDER BY "+sort+" desc,"+sort2+" desc",
        JsonArray.from(val));
	    
		N1qlQueryResult result = bucket.query(query);

		return result;
	}
	
}
