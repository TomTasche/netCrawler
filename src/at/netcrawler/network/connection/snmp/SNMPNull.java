package at.netcrawler.network.connection.snmp;


public class SNMPNull {
	
	private int syntax;
	private String syntaxString;
	
	public SNMPNull(int syntax, String syntaxString) {
		this.syntax = syntax;
		this.syntaxString = syntaxString;
	}
	
	@Override
	public String toString() {
		return syntaxString;
	}
	
	public int getSyntax() {
		return syntax;
	}
	
	public String getSyntaxString() {
		return syntaxString;
	}
	
}