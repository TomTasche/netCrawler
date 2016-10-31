package at.netcrawler.io.json;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;

import at.andiwand.library.network.mac.MACAddress;


public class JsonMACAddressAdapter extends JsonAdapter<MACAddress> {
	
	@Override
	public JsonElement serialize(MACAddress src, Type typeOfSrc,
			JsonSerializationContext context) {
		return new JsonPrimitive(src.toColonedString());
	}
	
	@Override
	public MACAddress deserialize(JsonElement json, Type typeOfT,
			JsonDeserializationContext context) throws JsonParseException {
		return new MACAddress(json.getAsJsonPrimitive().getAsString());
	}
	
}