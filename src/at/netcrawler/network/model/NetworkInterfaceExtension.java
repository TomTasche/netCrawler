package at.netcrawler.network.model;

import at.andiwand.library.network.ip.IPv4Address;
import at.andiwand.library.network.ip.SubnetMask;
import at.andiwand.library.network.mac.MACAddress;


public enum NetworkInterfaceExtension implements NetworkModelExtension {
	
	ETHERNET_ADDRESS {
		@Override
		public String getKey() {
			return "interface.ethernet.address";
		}
		
		@Override
		public Class<?> getType() {
			return MACAddress.class;
		}
	},
	
	IPV4_ADDRESS {
		@Override
		public String getKey() {
			return "interface.ipv4.address";
		}
		
		@Override
		public Class<?> getType() {
			return IPv4Address.class;
		}
	},
	IPV4_SUBNET_MASK {
		@Override
		public String getKey() {
			return "interface.ipv4.subnetMask";
		}
		
		@Override
		public Class<?> getType() {
			return SubnetMask.class;
		}
	};
	
}