package engine;

import java.awt.Component;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Array;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import engine.components.ServerTable;
import engine.components.clientTable;
import gui.components.ServerConsole;
import gui.components.TableModel;
import gui.components.View;

public class Server extends View {
	private static final long serialVersionUID = 1L;

	public Server(){
		try {
			new Thread(() -> load_Central_Server_Connection("127.0.0.1",6000,"Server1",6001)).start();
			Thread.sleep(500);
			new Thread(() -> load_Central_Server_Connection("127.0.0.1",6000,"Server2",6002)).start();
			Thread.sleep(500);
			new Thread(() -> load_Central_Server_Connection("127.0.0.1",6000,"Server3",6003)).start();
			Thread.sleep(500);
			new Thread(() -> load_Central_Server_Connection("127.0.0.1",6000,"Server4",6004)).start();
			} catch (InterruptedException exception) {
				exception.printStackTrace();
				}
	}
	
	private void load_Central_Server_Connection(String central, int portno, String serverName, int serverPort) {
		Socket socket;
		ObjectInputStream ois;
		ObjectOutputStream oos;
		ServerSocket serverSocket;
		Object oisData[],oosData[];
		ArrayList<clientTable> connectedClients ;
		try {
			socket =  new Socket(central, portno);
			serverSocket = new ServerSocket(serverPort);
			connectedClients = new ArrayList<clientTable>();
			oos = new ObjectOutputStream(socket.getOutputStream());
			ois = new ObjectInputStream(socket.getInputStream());
			new Thread(() -> start_Server_Connection(serverName,serverSocket,connectedClients,oos)).start();
			sendData(oos, oosData = new Object[]{"SERVER",serverName,serverPort,socket.getInetAddress().getHostAddress()});
			new Thread(() -> centralDataoos(serverName,connectedClients, oos)).start();
			while(true){
//				synchronized (ois) { 
					oisData = (Object[]) ois.readObject();
					System.out.println(oisData[0]+" gyhuh");
					switch(((String) oisData[0])){
					case "Route":
						System.out.println(serverName+" uhuhuhuhu");
						System.out.println(connectedClients.size());
						int index = getIndex(connectedClients, (String) oisData[2]);
						System.out.println(index+" "+(String) oisData[2]);
						if(index == -1) break;
						sendData(connectedClients.get(index).getOos(), new Object[]{"Message",((String)oisData[1])+":"+((String)oisData[3])});
//						sendData(connectedClients.get(index).getOos(), new Object[]{"Message",((String)oisData[2])+":"+((String)oisData[3])});
					}
//					}
			}
		} catch (IOException | ClassNotFoundException  e) {
			e.printStackTrace();
		}
		
	}

	private void centralDataoos(String serverName,ArrayList<clientTable> connectedClients, ObjectOutputStream oos ) {
		Object oosData[];
		while(true)
			try {
				oosData = new Object[connectedClients.size()+2];
				oosData[0] = "ServerAddClients";
				oosData[1] = serverName;
				for(int i = 2 ; i<oosData.length;i++){
					oosData[i]=connectedClients.get(i-2).getName();
				}
				sendData(oos, oosData);
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

	private void start_Server_Connection(String serverName, ServerSocket serverSocket, ArrayList<clientTable> connectedClients, ObjectOutputStream centraloos) {
		while(true){
			try {
				Socket socket =  serverSocket.accept();
				new Thread(() -> client_Connection(serverName,socket,connectedClients,centraloos)).start();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	}
	private int getIndex(ArrayList<clientTable> list,String name){
		for(int i = 0;i<list.size();i++)
			if(list.get(i).getName().equals(name))
				return i;
		return -1;
	}
	
	private void client_Connection(String serverName, Socket socket, ArrayList<clientTable> clients, ObjectOutputStream centraloos) {
		String name = null;
		ObjectInputStream ois;
		ObjectOutputStream oos;
		Object oisData[],oosData[];
		try {
			oos = new ObjectOutputStream(socket.getOutputStream());
			ois = new ObjectInputStream(socket.getInputStream());
			while(true){
//				synchronized (ois) { 
					oisData = (Object[]) ois.readObject();
					//System.out.println(oisData[0]+" Tag");
//					}
				switch(((String) oisData[0])){
				case "ClientName":
					name = (String) oisData[1];
					System.out.println(name);
					if(getIndex(clients, name)!=-1){
						sendData(oos, new Object[]{"Error","userName Exists"});break;
					}else
						clients.add(new clientTable(name, socket, oos));
					sendData(oos, new Object[]{"Accepted"});break;
				case "getMemberList":
					oosData = new Object[clients.size()+1];
					oosData[0] = "ConnectedClientsList";
					for(int i=1;i<oosData.length;i++){
						oosData[i]=clients.get(i-1).getName();
					}
					sendData(oos, oosData);break;
				case "Chat":
					if(getIndex(clients, (String)oisData[1])!=-1){
						sendData(clients.get(getIndex(clients, name)).getOos(), new Object[]{"Message",name +":"+ ((String)oisData[2])});
						sendData(clients.get(getIndex(clients, (String)oisData[1])).getOos(), new Object[]{"Message",name+":"+((String)oisData[2])});
					} else
					sendData(centraloos, new Object[]{"CentralRoute",serverName,name,(String)oisData[1],(String)oisData[2]});
					sendData(clients.get(getIndex(clients, name)).getOos(), new Object[]{"Message",name+":"+((String)oisData[2])});
					break;
				case "Data":
				}
			}
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new Server();
		
	}
}
