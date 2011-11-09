package at.netcrawler.network.model;

import at.netcrawler.network.RoutingTable;


public enum NetworkDeviceExtension implements NetworkModelExtension {
	
	ROUTER_ROUTING_TABLE {
		@Override
		public String getKey() {
			return "device.router.routingTable";
		}
		
		@Override
		public Class<?> getType() {
			return RoutingTable.class;
		}
	};
	
}