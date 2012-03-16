package at.netcrawler.io.json;

import java.lang.reflect.Type;
import java.net.InetAddress;
import java.net.UnknownHostException;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;


public class JsonInetAddressAdapter extends JsonAdapter<InetAddress> {
	
	@Override
	public JsonElement serialize(InetAddress src, Type typeOfSrc,
			JsonSerializationContext context) {
		String address = src.toString();

		return new JsonPrimitive(address.substring(address.lastIndexOf("/") + 1));
	}
	
	@Override
	public InetAddress deserialize(JsonElement json, Type typeOfT,
			JsonDeserializationContext context) throws JsonParseException {
		try {
			return InetAddress.getByName(json.getAsJsonPrimitive().getAsString());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
		return null;
	}
}