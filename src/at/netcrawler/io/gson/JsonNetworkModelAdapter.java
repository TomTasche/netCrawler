package at.netcrawler.io.gson;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import at.andiwand.library.util.TypeToken;
import at.andiwand.library.util.comparator.StringLengthComperator;
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
	private static final Type EXTENSION_TYPE = new TypeToken<Set<Class<? extends NetworkModelExtension>>>() {}
			.getType();
	private static final String VALUES_PROPERTY = "values";
	
	@Override
	public JsonElement serialize(NetworkModel src, Type typeOfSrc,
			JsonSerializationContext context) {
		Map<String, Object> valueMap = new TreeMap<String, Object>(src
				.getValueMap());
		Set<Class<?>> extensionClasses = new TreeSet<Class<?>>(
				new StringLengthComperator());
		
		for (NetworkModelExtension extension : src.getExtensions())
			extensionClasses.add(extension.getClass());
		
		JsonObject result = new JsonObject();
		result.add(EXTENSIONS_PROPERTY, context.serialize(extensionClasses));
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
			
			Set<Class<? extends NetworkModelExtension>> extensionClasses = context
					.deserialize(object.get(EXTENSIONS_PROPERTY),
							EXTENSION_TYPE);
			
			for (Class<? extends NetworkModelExtension> extensionClass : extensionClasses) {
				NetworkModelExtension extension = NetworkModelExtension
						.getInstance(extensionClass);
				result.addExtension(extension);
			}
			
			JsonObject valueMapObject = object.get(VALUES_PROPERTY)
					.getAsJsonObject();
			
			Map<String, TypeToken<?>> typeMap = result.getTypeMap();
			
			for (Entry<String, JsonElement> entry : valueMapObject.entrySet()) {
				String key = entry.getKey();
				JsonElement valueElement = entry.getValue();
				
				Object value = context.deserialize(valueElement, typeMap.get(
						key).getType());
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