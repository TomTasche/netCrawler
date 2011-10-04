package at.netcrawler.network.model;


public enum NetworkCableExtension implements NetworkModelExtension {
	
	ETHERNET_CROSSED {
		@Override
		public String getKey() {
			return "cable.ethernet.crossed";
		}
		
		@Override
		public Class<?> getType() {
			return Boolean.class;
		}
	};
	
}