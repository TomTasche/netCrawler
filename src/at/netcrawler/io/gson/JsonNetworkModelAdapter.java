package at.netcrawler.io.gson;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import at.andiwand.library.util.GenericsUtil;
import at.andiwand.library.util.StringComperator;
import at.netcrawler.network.model.NetworkModel;
import at.netcrawler.network.model.NetworkModelExtension;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;


// TODO: fix generics
public class JsonNetworkModelAdapter extends JsonAdapter<NetworkModel> {
	
	private static final String EXTENSIONS_PROPERTY = "extensions";
	private static final Type EXTENSION_TYPE = new GenericsUtil.TypeToken<Set<Class<? extends NetworkModelExtension>>>() {}.getType();
	private static final String VALUES_PROPERTY = "values";
	
	@Override
	public JsonElement serialize(NetworkModel src, Type typeOfSrc,
			JsonSerializationContext context) {
		Map<String, Object> valueMap = new TreeMap<String, Object>(
				src.getValueMap());
		Set<Class<?>> extensions = new TreeSet<Class<?>>(new StringComperator());
		
		for (NetworkModelExtension extension : src.getExtensions()) {
			extensions.add(extension.getClass());
		}
		
		JsonObject result = new JsonObject();
		result.add(EXTENSIONS_PROPERTY, context.serialize(extensions));
		result.add(VALUES_PROPERTY, context.serialize(valueMap));
		
		return result;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public NetworkModel deserialize(JsonElement json, Type typeOfT,
			JsonDeserializationContext context) throws JsonParseException {
		Class<? extends NetworkModel> modelClass = (Class<? extends NetworkModel>) typeOfT;
		JsonObject object = json.getAsJsonObject();
		
		try {
			NetworkModel result = modelClass.newInstance();
			
			Set<Class<? extends NetworkModelExtension>> extensions = context.deserialize(
					object.get(EXTENSIONS_PROPERTY), EXTENSION_TYPE);
			
			for (Class<? extends NetworkModelExtension> extension : extensions) {
				result.addExtension(extension);
			}
			
			JsonObject valueMapObject = object.get(VALUES_PROPERTY).getAsJsonObject();
			
			Map<String, Type> typeMap = result.getTypeMap();
			
			for (Entry<String, JsonElement> entry : valueMapObject.entrySet()) {
				String key = entry.getKey();
				JsonElement valueElement = entry.getValue();
				
				Object value = context.deserialize(valueElement,
						typeMap.get(key));
				result.setValue(key, value);
			}
			
			return result;
		} catch (JsonParseException e) {
			throw e;
		} catch (Exception e) {
			throw new JsonParseException(e);
		}
	}
	
}