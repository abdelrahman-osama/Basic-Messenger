package engine.components;

import java.io.ObjectOutputStream;
import java.net.Socket;

public class clientTable {
	private String name;
	private Socket socket;
	private ObjectOutputStream oos;
	public clientTable(String name, Socket socket, ObjectOutputStream oos) {
		this.name = name;
		this.socket = socket;
		this.oos = oos;
	}
	public String getName() {
		return name;
	}
	public Socket getSocket() {
		return socket;
	}
	public ObjectOutputStream getOos() {
		return oos;
	}
}
