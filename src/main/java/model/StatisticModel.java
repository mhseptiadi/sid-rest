package model;
import java.util.LinkedHashMap;

public class StatisticModel {
	public WaterPoint waterPoint;
    public LinkedHashMap<String, Community> community;
    public LinkedHashMap<String, CommunityRank> communityRankGood;
    public LinkedHashMap<String, CommunityRank> communityRankBad;

	public StatisticModel() {
    	this.community = new LinkedHashMap<String, Community>();
    	this.communityRankGood = new LinkedHashMap<String, CommunityRank>();
    	this.communityRankBad = new LinkedHashMap<String, CommunityRank>();
    }
    
	public void setWaterPoint(WaterPoint waterPoint) {
		this.waterPoint = waterPoint;
	}
    
	public void setCommunity(String key, Community community) {
		this.community.put(key, community);
	}

	public void setCommunityRankGood(String key, CommunityRank communityRank) {
		this.communityRankGood.put(key, communityRank);
	}
	
	public void setCommunityRankBad(String key, CommunityRank communityRank) {
		this.communityRankBad.put(key, communityRank);
	}
}