package engine.components;

import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class ServerTable {
	private String name;
	private int portno;
	private String ipAddress;
	private ObjectOutputStream oos;
	private ArrayList<String> clients;
	
	public ServerTable(String name,int portno, String ipAddress){
		this.name=name;
		this.portno = portno;
		this.ipAddress = ipAddress;
		clients = new ArrayList<String>();
	}
	
	public void setOos(ObjectOutputStream oos) {
		this.oos = oos;
	}
	
	public ObjectOutputStream getOos() {
		return oos;
	}

	public ArrayList<String> getClients(){
		return clients;
	}
	
	public String getName() {
		return name;
	}
	
	public int getPortno() {
		return portno;
	}
	
	public String getIpAddress() {
		return ipAddress;
	}

}
