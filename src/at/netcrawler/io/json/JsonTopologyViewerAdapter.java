package at.netcrawler.io.json;

import java.awt.Point;
import java.lang.reflect.Type;
import java.util.Map;

import at.andiwand.library.component.GraphViewerVertex;
import at.andiwand.library.util.TypeToken;
import at.netcrawler.network.topology.Topology;
import at.netcrawler.network.topology.TopologyDevice;
import at.netcrawler.ui.component.TopologyViewer;
import at.netcrawler.ui.component.TopologyViewerDevice;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;


public class JsonTopologyViewerAdapter extends JsonAdapter<TopologyViewer> {
	
	private static final String TOPOLOGY_PROPERTY = "topology";
	private static final Type TOPOLOGY_TYPE = new TypeToken<Topology>() {}
			.getType();
	
	private static final String VERTEX_POSITIONS = "vertexPositions";
	private static final Type VERTEX_POSITIONS_ELEMENT_TYPE = new TypeToken<Point>() {}
			.getType();
	
	@Override
	public JsonElement serialize(TopologyViewer src, Type typeOfSrc,
			JsonSerializationContext context) {
		JsonObject result = new JsonObject();
		
		result.add(TOPOLOGY_PROPERTY, context.serialize(src.getModel()));
		
		JsonObject vertexPositions = new JsonObject();
		result.add(VERTEX_POSITIONS, vertexPositions);
		
		for (GraphViewerVertex vertex : src.getGraphViewerVertices()) {
			TopologyViewerDevice topologyViewerDevice = (TopologyViewerDevice) vertex;
			TopologyDevice topologyDevice = topologyViewerDevice
					.getTopologyDevice();
			String deviceName = JsonTopologyAdapter
					.getSerializedDeviceName(topologyDevice);
			
			vertexPositions.add(deviceName, context.serialize(vertex
					.getPosition()));
		}
		
		return null;
	}
	
	@Override
	public TopologyViewer deserialize(JsonElement json, Type typeOfT,
			JsonDeserializationContext context) throws JsonParseException {
		JsonObject object = json.getAsJsonObject();
		
		Topology topology = context.deserialize(object.get(TOPOLOGY_PROPERTY),
				TOPOLOGY_TYPE);
		TopologyViewer result = new TopologyViewer(topology);
		
		JsonObject vertexPositions = object.get(VERTEX_POSITIONS)
				.getAsJsonObject();
		
		for (Map.Entry<String, JsonElement> vertexPosition : vertexPositions
				.entrySet()) {
			String deviceName = vertexPosition.getKey();
			Point position = context.deserialize(vertexPosition.getValue(),
					VERTEX_POSITIONS_ELEMENT_TYPE);
			
			TopologyDevice topologyDevice = JsonTopologyAdapter
					.getDeserializedTopologyDevice(deviceName);
			GraphViewerVertex graphViewerVertex = result
					.getGraphViewerVertex(topologyDevice);
			graphViewerVertex.setPosition(position);
		}
		
		return result;
	}
	
}