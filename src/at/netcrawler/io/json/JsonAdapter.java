package at.netcrawler.io.json;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;


public abstract class JsonAdapter<T> implements JsonSerializer<T>,
		JsonDeserializer<T> {
	
	@Override
	public abstract JsonElement serialize(T src, Type typeOfSrc,
			JsonSerializationContext context);
	
	@Override
	public abstract T deserialize(JsonElement json, Type typeOfT,
			JsonDeserializationContext context) throws JsonParseException;
	
}