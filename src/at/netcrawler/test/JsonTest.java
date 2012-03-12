package at.netcrawler.test;

import java.util.HashMap;
import java.util.Map;

import at.andiwand.library.network.ip.IPv4Address;

import com.google.gson.Gson;


public class JsonTest {
	
	public static void main(String[] args) {
		Gson gson = new Gson();
		
		Map<String, Object> valueMap = new HashMap<String, Object>();
		valueMap.put("name", "das ist ein name");
		valueMap.put("upTime", 1002);
		valueMap.put("ip", new IPv4Address("10.0.0.1"));
		
		System.out.println(gson.toJson(valueMap));
	}
	
}