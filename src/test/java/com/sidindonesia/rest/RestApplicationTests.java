package com.sidindonesia.rest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.query.N1qlQueryResult;

import model.StatisticModel;
import service.ParseDataService;
import service.WaterPointService;

@RunWith(SpringRunner.class)
@SpringBootTest
@ComponentScan(basePackages = {"org.springframework.test.web.reactive.server"})
public class RestApplicationTests {
	
	private static ParseDataService pds;
	private WaterPointService wps;

	@Test
	public void testServiceConnToCouchDB() {
		Bucket bucket = pds.bucket;
        Assert.assertNotNull("failure - expected not null", bucket);
	}
	
	@BeforeClass
	public static void testServiceParseData() {
		String res = null;
		try {
			res = pds.parseSource("https://raw.githubusercontent.com/onaio/ona-tech/master/data/water_points.json");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Assert.assertEquals("failure - expected ok", "ok", res );
	}

	@Test
	public void testServiceGetStat() {
		StatisticModel res = null;
		try {
			res = wps.stat();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Assert.assertNotNull(res);
		Assert.assertNotNull(res.community);
		Assert.assertNotNull(res.waterPoint);
		Assert.assertNotNull(res.communityRankGood);
		Assert.assertNotNull(res.communityRankBad);
	}
	
	@Test
	public void testServiceGetWaterPoint() {

		N1qlQueryResult res = null;
		try {
			res = wps.waterPoint("yes");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Assert.assertNotNull(res);
		Assert.assertNotNull(res.allRows());
		Assert.assertNotNull(res.allRows().get(0).value().get("waterPointTotal").toString());
		Assert.assertNotNull(res.allRows().get(0).value().get("waterFunctioningNo").toString());
		Assert.assertNotNull(res.allRows().get(0).value().get("waterFunctioningYes").toString());
		Assert.assertNotNull(res.allRows().get(0).value().get("percent").toString());
	}

	@Test
	public void testServiceGetWaterPointPerCommunity() {

		N1qlQueryResult res = null;
		try {
			res = wps.wpPerCommunity("yes","total","waterFunctioningYes");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Assert.assertNotNull(res);
		Assert.assertNotNull(res.allRows());
		Assert.assertNotNull(res.allRows().get(0).value().get("total").toString());
		Assert.assertNotNull(res.allRows().get(0).value().get("waterFunctioningNo").toString());
		Assert.assertNotNull(res.allRows().get(0).value().get("waterFunctioningYes").toString());
		Assert.assertNotNull(res.allRows().get(0).value().get("percent").toString());
		Assert.assertNotNull(res.allRows().get(0).value().get("communities_villages").toString());
	}
	
	

	@Autowired
	private WebApplicationContext webApplicationContext;
	
	private MockMvc mockMvc;
	
	@Before
	public void setup() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}
	
	@Test
	public void testWebRoot() throws Exception {
		mockMvc.perform(get("/"))
				.andExpect(status().isOk())
				.andExpect(content().contentType("text/plain;charset=UTF-8"))
				.andExpect(content().string("ok"));
	}
	
	@Test
	public void testWebStat() throws Exception {
		mockMvc.perform(get("/stat"))
		.andExpect(status().isOk())
		.andExpect(content().contentType("application/json;charset=UTF-8"))
		.andExpect(content().json("{\"waterPoint\":{},\"community\":{},\"communityRankGood\":{},\"communityRankBad\":{}}", false));
	}
	

	@Test
	public void testWebWaterPoint() throws Exception {
		mockMvc.perform(get("/wp/yes"))
		.andExpect(status().isOk())
		.andExpect(content().contentType("application/json;charset=UTF-8"));
	}


	@Test
	public void testWebWaterPointPerCommunity() throws Exception {
		mockMvc.perform(get("/wp-per-comm/total"))
		.andExpect(status().isOk())
		.andExpect(content().contentType("application/json;charset=UTF-8"));
	}

	

	
}
