import java.util.ArrayList;

public class Peer {
	private String ip;
	private ArrayList<String> resources;
	
	public Peer(String ip, ArrayList<String> resources) {
		super();
		this.ip = ip;
		this.resources = resources;
	}
	public String getIp() {
		return ip;
	}
	public ArrayList<String> getResources() {
		return resources;
	}
	
	
	
}
