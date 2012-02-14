package at.netcrawler.io.gson;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;

import at.andiwand.library.network.ip.SubnetMask;


public class JsonSubnetMaskAdapter extends JsonAdapter<SubnetMask> {
	
	@Override
	public JsonElement serialize(SubnetMask src, Type typeOfSrc,
			JsonSerializationContext context) {
		return new JsonPrimitive(src.toDottedString());
	}
	
	@Override
	public SubnetMask deserialize(JsonElement json, Type typeOfT,
			JsonDeserializationContext context) throws JsonParseException {
		return new SubnetMask(json.getAsJsonPrimitive().getAsString());
	}
	
}