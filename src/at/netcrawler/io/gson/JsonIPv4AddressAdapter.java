package at.netcrawler.io.gson;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;

import at.andiwand.library.network.ip.IPv4Address;


public class JsonIPv4AddressAdapter extends JsonAdapter<IPv4Address> {
	
	@Override
	public JsonElement serialize(IPv4Address src, Type typeOfSrc,
			JsonSerializationContext context) {
		return new JsonPrimitive(src.toString());
	}
	
	@Override
	public IPv4Address deserialize(JsonElement json, Type typeOfT,
			JsonDeserializationContext context) throws JsonParseException {
		return IPv4Address
				.getByAddress(json.getAsJsonPrimitive().getAsString());
	}
	
}