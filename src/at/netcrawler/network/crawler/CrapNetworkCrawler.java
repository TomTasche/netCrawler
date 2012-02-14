package at.netcrawler.network.crawler;

import java.io.IOException;
import java.util.Map;

import at.andiwand.library.cli.CommandLineInterface;
import at.andiwand.library.network.ip.IPv4Address;
import at.netcrawler.cli.agent.CLIAgentFactory;
import at.netcrawler.cli.agent.CiscoCLIAgentFactory;
import at.netcrawler.cli.agent.CiscoCLIAgentSettings;
import at.netcrawler.cli.agent.PromtPatternCLIAgent;
import at.netcrawler.network.accessor.IPDeviceAccessor;
import at.netcrawler.network.connection.ssh.LocalSSHGateway;
import at.netcrawler.network.connection.ssh.SSHGateway;
import at.netcrawler.network.connection.ssh.SSHSettings;
import at.netcrawler.network.manager.DeviceManager;
import at.netcrawler.network.manager.cli.CiscoCLIDeviceManager;
import at.netcrawler.network.model.NetworkDevice;
import at.netcrawler.network.model.NetworkInterface;
import at.netcrawler.network.topology.Topology;
import at.netcrawler.network.topology.TopologyDevice;
import at.netcrawler.network.topology.TopologyInterface;


public class CrapNetworkCrawler extends NetworkCrawler {
	
	private IPv4Address root;
	private SSHSettings settings;
	private SSHGateway gateway = new LocalSSHGateway();
	private CiscoCLIAgentSettings agentSettings;
	private CLIAgentFactory agentFactory = new CiscoCLIAgentFactory();
	
	public CrapNetworkCrawler(IPv4Address root, SSHSettings settings,
			CiscoCLIAgentSettings agentSettings) {
		this.root = root;
		this.settings = settings;
		this.agentSettings = agentSettings;
	}
	
	@Override
	public void crawl(Topology topology) throws IOException {
		crawlImpl(topology, root, null);
	}
	
	private void crawlImpl(Topology topology, IPv4Address address,
			TopologyInterface source) throws IOException {
		IPDeviceAccessor accessor = new IPDeviceAccessor(address);
		CommandLineInterface cli = (CommandLineInterface) gateway.openConnection(
				accessor, settings);
		PromtPatternCLIAgent agent = (PromtPatternCLIAgent) agentFactory.createAgent(
				cli, agentSettings);
		
		NetworkDevice device = new NetworkDevice();
		DeviceManager manager = new CiscoCLIDeviceManager(device, agent);
		manager.updateDevice();
		
		TopologyDevice topologyDevice = new TopologyDevice(device);
		if (!topology.addVertex(topologyDevice)) return;
		
		Map<IPv4Address, NetworkInterface> neighbors = manager.discoverNeighbors();
		for (Map.Entry<IPv4Address, NetworkInterface> neighbor : neighbors.entrySet()) {
			NetworkInterface interfaze = neighbor.getValue();
			String interfaceName = (String) interfaze.getValue(NetworkInterface.NAME);
			TopologyInterface topologyInterface = topologyDevice.getInterfaceByName(interfaceName);
			
			crawlImpl(topology, neighbor.getKey(), topologyInterface);
		}
	}
	
}