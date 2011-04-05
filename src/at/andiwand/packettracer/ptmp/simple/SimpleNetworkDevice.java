package at.andiwand.packettracer.ptmp.simple;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Random;

import at.andiwand.library.network.MACAddress;
import at.andiwand.library.network.SimpleMACAddressFormat;


public class SimpleNetworkDevice {
	
	public static final SimpleMACAddressFormat MACADDRESS_FORMAT =
		new SimpleMACAddressFormat("XXXX.XXXX.XXXX");
	
	
	
	private SimpleMultiuserClient multiuserClient;
	private int linkId;
	
	private String name;
	private MACAddress macAddress;
	private Inet4Address inet4Address;
	private Inet4Address defaultRoute;
	
	private Queue<String[]> frameQueue;
	private FrameHandler frameHandler;
	private Object frameMonitor = new Object();
	
	private Random sourceRandom = new Random();
	
	private Map<Inet4Address, MACAddress> arpTable =
		new HashMap<Inet4Address, MACAddress>();
	private Map<Inet4Address, Integer> identificationTable =
		new HashMap<Inet4Address, Integer>();
	protected Map<Integer, SimpleTelnetConnection> telnetConnections =
		new HashMap<Integer, SimpleTelnetConnection>();
	
	private boolean up;
	
	
	
	public SimpleNetworkDevice(String name, Inet4Address inet4Address) {
		this(name, MACAddress.randomAddress(), inet4Address);
	}
	public SimpleNetworkDevice(String name, MACAddress macAddress, Inet4Address inet4Address) {
		this.name = name;
		this.macAddress = macAddress;
		this.inet4Address = inet4Address;
	}
	
	
	
	@Override
	public String toString() {
		return name;
	}
	
	
	public String getName() {
		return name;
	}
	public MACAddress getMacAddress() {
		return macAddress;
	}
	public Inet4Address getInet4Address() {
		return inet4Address;
	}
	
	public boolean isUp() {
		return up;
	}
	
	protected void setMultiuserClient(SimpleMultiuserClient multiuserClient, int linkId) {
		this.multiuserClient = multiuserClient;
		this.linkId = linkId;
		
		if (multiuserClient == null) {
			stop();
		} else {
			start();
		}
	}
	public void setDefaultRoute(Inet4Address defaultRoute) {
		this.defaultRoute = defaultRoute;
	}
	
	
	public void queueFrame(String[] frame) {
		if (!frame[0].equals("CEtherentIIHeader")) return;
		
		synchronized (frameQueue) {
			frameQueue.add(frame);
		}
		
		synchronized (frameMonitor) {
			frameMonitor.notify();
		}
	}
	
	public void receiveFrame(String[] frame) throws IOException {
		if (!frame[0].equals("CEtherentIIHeader")) return;
		
		try {
			//MACAddress sourceAddress = MACADDRESS_FORMAT.parseObject(frame[frame.length - 4]);
			MACAddress destinationAddress = MACADDRESS_FORMAT.parseObject(frame[frame.length - 3]);
			
			if (!destinationAddress.equals(MACAddress.BROADCAST_ADDRESS) &&
					!destinationAddress.equals(macAddress)) return;
			
			String[] payload = Arrays.copyOfRange(frame, 1, frame.length - 4);
			
			if (payload[0].equals("CArpPacket")) {
				handleArp(payload);
			} else if (payload[0].equals("CIpHeader")) {
				receivePacket(payload);
			}
		} catch (ParseException e) {}
	}
	public void receivePacket(String[] packet) throws IOException {
		try {
			Inet4Address sourceAddress = (Inet4Address) Inet4Address.getByName(packet[packet.length - 2]);
			Inet4Address destinationAddress = (Inet4Address) Inet4Address.getByName(packet[packet.length - 1]);
			
			if (!destinationAddress.equals(inet4Address)) return;
			
			String[] payload = Arrays.copyOfRange(packet, 1, packet.length - 14);
			
			if (payload[0].equals("CIcmpMessage")) {
				handleIcmp(sourceAddress, payload);
			} else if (payload[0].equals("CTcpHeader")) {
				receiveSegment(payload);
			}
		} catch (UnknownHostException e) {}
	}
	public void receiveSegment(String[] segment) throws IOException {
		int destinationPort;
		
		if (segment[1].equals("CTelnetPacket")) {
			destinationPort = Integer.parseInt(segment[6]);
		} else if (segment[1].equals("CPduGroup")) {
			int count = Integer.parseInt(segment[2]);
			destinationPort = Integer.parseInt(segment[2 + count * 4 + 2]);
		} else {
			destinationPort = Integer.parseInt(segment[3]);
		}
		
		if (!telnetConnections.containsKey(destinationPort)) return;
		
		SimpleTelnetConnection telnetConnection = telnetConnections.get(destinationPort);
		telnetConnection.handleSegment(segment);
	}
	
	public void sendFrame(Object[] frame) throws IOException {
		multiuserClient.sendFrame(linkId, frame);
	}
	public void sendFrame(MACAddress destinationAddresses, int etherType, Object[] payload) throws IOException {
		Object[] frame = {"CEtherentIIHeader", payload, macAddress, destinationAddresses, 0, etherType};
		sendFrame(frame);
	}
	public void sendPacket(int protocol, Inet4Address destinationAddresses, Object[] payload) throws IOException {
		MACAddress destinationMac = arpRequest(destinationAddresses);
		if (destinationMac == null) {
			destinationMac = arpRequest(defaultRoute);
			
			if (destinationMac == null)
				throw new RuntimeException("cannot resolve " + destinationAddresses.getHostAddress());
		}
		
		if (!identificationTable.containsKey(destinationAddresses))
			identificationTable.put(destinationAddresses, 0);
		
		int identification = identificationTable.get(destinationAddresses);
		
		Object[] packet = {"CIpHeader", payload, 4, 5, 0, 128, identification, 0, 0, 255, protocol,
				0, 0, 0, inet4Address, destinationAddresses};
		
		identification++;
		identificationTable.put(destinationAddresses, identification);
		
		sendFrame(destinationMac, 2048, packet);
	}
	
	
	private void handleArp(String[] packet) throws IOException {
		int operation = Integer.parseInt(packet[5]);
		
		try {
			MACAddress srcMac = MACADDRESS_FORMAT.parseObject(packet[6]);
			//MACAddress dstMac = MACADDRESS_FORMAT.parseObject(packet[7]);
			Inet4Address srcIp = (Inet4Address) Inet4Address.getByName(packet[8]);
			Inet4Address dstIp = (Inet4Address) Inet4Address.getByName(packet[9]);
			
			if (!dstIp.equals(inet4Address)) return;
			
			switch (operation) {
			case 1:
				sendArp(srcMac, 2, macAddress, srcMac, inet4Address, srcIp);
				break;
			case 2:
				arpTable.put(srcIp, srcMac);
				break;
			}
		} catch (ParseException e) {
		} catch (UnknownHostException e) {}
	}
	private void sendArp(MACAddress destinationAddress, int operation, MACAddress srcMac, MACAddress dstMac, Inet4Address srcIp, Inet4Address dstIp) throws IOException {
		Object[] arp = {"CArpPacket", 1, 2048, 6, 4, operation, srcMac, dstMac, srcIp, dstIp};
		
		sendFrame(destinationAddress, 2054, arp);
	}
	public MACAddress arpRequest(Inet4Address ip) throws IOException {
		for (int i = 0; i < 4; i++) {
			if (arpTable.containsKey(ip)) return arpTable.get(ip);
			
			sendArp(MACAddress.BROADCAST_ADDRESS, 1, macAddress, MACAddress.BROADCAST_ADDRESS, inet4Address, ip);
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {}
		}
		
		return null;
	}
	
	
	public void handleIcmp(final Inet4Address sourceAddress, String[] packet) throws IOException {
		int type = Integer.parseInt(packet[1]);
		
		switch (type) {
		case 8:
			final int identifier = Integer.parseInt(packet[4]);
			final int sequenceNumber = Integer.parseInt(packet[5]);
			new Thread() {
				public void run() {
					try {
						sendIcmpReply(sourceAddress, identifier, sequenceNumber);
					} catch (IOException e) {}
				}
			}.start();
			break;
		}
	}
	public void sendIcmpReply(Inet4Address destinationAddress, int identifier, int sequenceNumber) throws IOException {
		Object[] icmp = {"CIcmpMessage", 0, 0, 0, identifier, sequenceNumber};
		
		sendPacket(1, destinationAddress, icmp);
	}
	
	
	public SimpleTelnetConnection createTelnetConnection(Inet4Address address) throws IOException {
		int sourcePort = 1024 + sourceRandom.nextInt(10000);
		SimpleTelnetConnection telnetConnection = new SimpleTelnetConnection(this, sourcePort, address, 23);
		telnetConnection.connect();
		return telnetConnection;
	}
	
	
	public void start() {
		if (up) return;
		
		frameQueue = new LinkedList<String[]>();
		frameHandler = new FrameHandler();
		
		up = true;
	}
	
	public void stop() {
		if (!up) return;
		
		frameHandler.interrupt();
		try {
			frameHandler.join();
		} catch (InterruptedException e) {}
		
		frameQueue = null;
		
		up = false;
	}
	
	
	
	private class FrameHandler extends Thread {
		public FrameHandler() {
			start();
		}
		
		public void run() {
			try {
				while (!isInterrupted()) {
					if (frameQueue.isEmpty()) {
						synchronized (frameMonitor) {
							frameMonitor.wait();
						}
					}
					
					synchronized (frameQueue) {
						receiveFrame(frameQueue.poll());
					}
				}
			} catch (InterruptedException e) {
			} catch (IOException e) {}
		}
	}
	
}