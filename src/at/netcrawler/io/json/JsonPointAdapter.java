package at.netcrawler.io.json;

import java.awt.Point;
import java.lang.reflect.Type;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;


public class JsonPointAdapter extends JsonAdapter<Point> {
	
	private static final Pattern POINT_PATTERN = Pattern
			.compile("\\(\\s*(\\d+)\\s*,\\s*(\\d+)\\s*\\)");
	
	@Override
	public JsonElement serialize(Point src, Type typeOfSrc,
			JsonSerializationContext context) {
		return new JsonPrimitive("(" + src.getX() + ", " + src.getY() + ")");
	}
	
	@Override
	public Point deserialize(JsonElement json, Type typeOfT,
			JsonDeserializationContext context) throws JsonParseException {
		String string = json.getAsString();
		Matcher matcher = POINT_PATTERN.matcher(string);
		if (!matcher.matches())
			throw new JsonParseException("Malformed point");
		int x = Integer.parseInt(matcher.group(1));
		int y = Integer.parseInt(matcher.group(2));
		return new Point(x, y);
	}
	
}