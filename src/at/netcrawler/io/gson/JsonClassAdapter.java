package at.netcrawler.io.gson;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;


public class JsonClassAdapter extends JsonAdapter<Class<?>> {
	
	@Override
	public JsonElement serialize(Class<?> src, Type typeOfSrc,
			JsonSerializationContext context) {
		return new JsonPrimitive(src.getName());
	}
	
	@Override
	public Class<?> deserialize(JsonElement json, Type typeOfT,
			JsonDeserializationContext context) throws JsonParseException {
		try {
			return Class.forName(json.getAsString());
		} catch (ClassNotFoundException e) {
			throw new JsonParseException(e);
		}
	}
	
}