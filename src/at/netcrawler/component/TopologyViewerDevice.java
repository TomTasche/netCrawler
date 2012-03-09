package at.netcrawler.component;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import at.andiwand.library.component.GraphViewerVertex;
import at.andiwand.library.graphics.GraphicsUtil;
import at.netcrawler.network.Capability;
import at.netcrawler.network.model.NetworkDevice;
import at.netcrawler.network.model.NetworkModelAdapter;
import at.netcrawler.network.topology.TopologyDevice;


public class TopologyViewerDevice extends GraphViewerVertex {
	
	private static enum DeviceImage {
		UNKNOWN(null, "unknown.png"), ROUTER(Capability.ROUTER, "router.png"),
		SWITCH(Capability.SWITCH, "switch.png");
		
		private static final Map<Capability, DevicePainter> PAINTER_MAP = new HashMap<Capability, DevicePainter>();
		
		static {
			for (DeviceImage deviceImage : values()) {
				PAINTER_MAP.put(
						deviceImage.capability, deviceImage.painter);
			}
		}
		
		public static Map<Capability, DevicePainter> getPainterMap() {
			return new HashMap<Capability, DevicePainter>(PAINTER_MAP);
		}
		
		private static Image loadImage(String imageName) {
			try {
				return ImageIO.read(TopologyViewerDevice.class
						.getResource(imageName));
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		
		private final Capability capability;
		private final DevicePainter painter;
		
		private DeviceImage(Capability capability, String imageName) {
			this(capability, loadImage(imageName));
		}
		
		private DeviceImage(Capability capability, Image image) {
			this(capability, new ImageDevicePainter(image));
		}
		
		private DeviceImage(Capability capability, DevicePainter painter) {
			this.capability = capability;
			this.painter = painter;
		}
	}
	
	private static final int HOSTNAME_GAP_Y = 5;
	
	private class HostnameAdapter extends NetworkModelAdapter {
		public void valueChanged(String key, Object value, Object oldValue) {
			if (!key.equals(NetworkDevice.HOSTNAME)) return;
			hostname = (String) value;
		}
	}
	
	private class CapabilityAdapter extends NetworkModelAdapter {
		public void valueChanged(String key, Object value, Object oldValue) {
			if (!key.equals(NetworkDevice.MAJOR_CAPABILITY)) return;
			setCapability((Capability) value);
		}
	}
	
	private final Map<Capability, DevicePainter> painterMap = new HashMap<Capability, DevicePainter>(
			DeviceImage.getPainterMap());
	
	private final NetworkDevice networkDevice;
	private String hostname;
	private Capability capability;
	private DevicePainter painter;
	
	public TopologyViewerDevice(TopologyDevice device) {
		super(device);
		
		this.networkDevice = device.getNetworkDevice();
		this.hostname = (String) networkDevice.getValue(NetworkDevice.HOSTNAME);
		setCapability((Capability) networkDevice
				.getValue(NetworkDevice.MAJOR_CAPABILITY));
		
		networkDevice.addListener(new HostnameAdapter());
		networkDevice.addListener(new CapabilityAdapter());
	}
	
	@Override
	public Dimension getSize() {
		return painter.getSize();
	}
	
	public TopologyDevice getTopologyDevice() {
		return (TopologyDevice) getVertex();
	}
	
	public NetworkDevice getNetworkDevice() {
		return networkDevice;
	}
	
	public DevicePainter getPainter(Capability capability) {
		DevicePainter painter = painterMap.get(capability);
		if (painter == null) painter = painterMap.get(null);
		return painter;
	}
	
	private void setCapability(Capability capability) {
		this.capability = capability;
		painter = getPainter(capability);
		
		revalidate();
		fireRepaint();
	}
	
	public void setPainter(Capability capability, DevicePainter painter) {
		painterMap.put(
				capability, painter);
		setCapability(this.capability);
	}
	
	@Override
	public void paint(Graphics g) {
		painter.paint(
				g, this);
		
		Rectangle bounds = getBounds();
		Point xCenter = new Point(getMiddle().x, bounds.y + bounds.height
				+ HOSTNAME_GAP_Y);
		
		if (hostname == null) hostname = "Loading...";
		GraphicsUtil graphicsUtil = new GraphicsUtil(g);
		graphicsUtil.drawXCenterString(
				xCenter, hostname);
	}
	
}