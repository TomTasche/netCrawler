package at.netcrawler.io.json;

import java.lang.reflect.Type;

import at.netcrawler.network.topology.Topology;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;


public abstract class JsonTopologyAdapter<T extends Topology> extends
		JsonAdapter<T> {
	
	public abstract T getInstance();
	
	public JsonElement serialize(T src, Type typeOfSrc,
			JsonSerializationContext context) {
		return null;
	}
	
	@Override
	public T deserialize(JsonElement json, Type typeOfT,
			JsonDeserializationContext context) throws JsonParseException {
		return null;
	}
	
}