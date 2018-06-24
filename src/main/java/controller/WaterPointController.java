package controller;

import java.io.IOException;
import java.util.Arrays;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.couchbase.client.java.query.N1qlQueryResult;

import model.StatisticModel;
import service.ParseDataService;
import service.WaterPointService;

@RestController
public class WaterPointController {
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String insertData() throws IOException {
		String ret = ParseDataService.parseSource("https://raw.githubusercontent.com/onaio/ona-tech/master/data/water_points.json");
		return ret; 
	}
	
	//http://localhost:8080/stat
	@RequestMapping(value = "/stat", method = RequestMethod.GET, produces = "application/json")
	public StatisticModel stat() throws IOException {
		StatisticModel ret = WaterPointService.stat();
		return ret; 
	}

	//http://localhost:8080/wp/yes
	//http://localhost:8080/wp/no
	@RequestMapping(value = "/wp/{val}", method = RequestMethod.GET, produces = "application/json")
	public String waterPoint(@PathVariable("val") String val) throws IOException {
		String[] allow = new String[] {"yes","no"};
		if(!Arrays.asList(allow).contains(val)) {
			val = "yes";//default yes
		}
		N1qlQueryResult ret = WaterPointService.waterPoint(val);
		return ret.allRows().toString(); 
	}

	//http://localhost:8080/wp-per-comm/total
	//http://localhost:8080/wp-per-comm/yes
	//http://localhost:8080/wp-per-comm/no
	@RequestMapping(value = "/wp-per-comm/{val}", method = RequestMethod.GET, produces = "application/json")
	public String wpPerCommunity(@PathVariable("val") String val) throws IOException {
		String sort = "percent";
		if (new String(val).equals("total")) {
			sort = "total";
			val = "yes";//Override val value
		}
		String[] allow = new String[] {"yes","no"};
		if(!Arrays.asList(allow).contains(val)) {
			val = "yes";//default no
		}
		String sort2 = "waterFunctioningYes";
		if (new String(val).equals("no")) {
			sort2 = "waterFunctioningNo";
		}
		N1qlQueryResult ret = WaterPointService.wpPerCommunity(val,sort,sort2);
		return ret.allRows().toString(); 
	}
	
	
}
