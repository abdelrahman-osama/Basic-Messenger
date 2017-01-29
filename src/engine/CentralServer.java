package engine;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import engine.components.ServerTable;

public class CentralServer {
	private ArrayList<ServerTable> connectedservers;
	public CentralServer(int portno){
		ServerSocket serverSocket;
		connectedservers = new ArrayList<ServerTable>();
		try {
			serverSocket = new ServerSocket(portno);
			while(true){
				Socket socket = serverSocket.accept();
				new Thread(() -> new_Connection(socket)).start();
				}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private void new_Connection(Socket socket) {
		ObjectInputStream ois;
		ObjectOutputStream oos;
		Object oisData[],oosData[];
		try {
			oos = new ObjectOutputStream(socket.getOutputStream());
			ois = new ObjectInputStream(socket.getInputStream());
			while(true){
				oisData = (Object[]) ois.readObject();
				//System.out.println((String)oisData[0]+" TAG");
				switch(((String)oisData[0])){
				case "SERVER":
					ServerTable servertable = new ServerTable((String)oisData[1], (Integer)oisData[2],(String)oisData[3]);
					connectedservers.add(servertable);
					servertable.setOos(oos);
					break;
				case "ServerAddClients":
					for(int i=0;i<connectedservers.size();i++){
						if(((String)oisData[1]).equals(connectedservers.get(i).getName())){
							connectedservers.get(i).getClients().clear();
							for(int j = 2 ;j<oisData.length;j++){
								connectedservers.get(i).getClients().add((String) oisData[j]);
							}
						}
					}break;
				case "ServerGetClients":
					for(int i=0;i<connectedservers.size();i++){
						if(((String)oisData[1]).equals(connectedservers.get(i).getName())){
							oosData = new Object[connectedservers.get(i).getClients().size()+2];
							oosData[0]="ServerClients";
							oosData[1]=connectedservers.get(i).getName();
							for(int j=2;j<oosData.length;j++){
								oosData[j]=connectedservers.get(i).getClients().get(j-2);
							}
							sendData(oos, oosData);
						}
					}break;
					
				case "allMembers":
					ArrayList<String> total = new ArrayList<String>();
					for(int i=0;i<connectedservers.size();i++){
						for(int j=0;j<connectedservers.get(i).getClients().size();j++)
							total.add(connectedservers.get(i).getClients().get(j));
						oosData = new Object[total.size()+1];
						oosData[0]="connectedMembers";
						for(int k=1;k<oosData.length;k++)
							oosData[k]=total.get(k-1);
						sendData(oos, oosData);
					}break;
				case "SERVERSLIST":
					int j = 0;
					oosData = new Object[(connectedservers.size()*3)+1];
					oosData[0]= "SERVERS";
					for(int i = 1;i<oosData.length;i+=3){
						oosData[i]=connectedservers.get(j).getName();
						oosData[i+1]=connectedservers.get(j).getPortno();
						oosData[i+2]=connectedservers.get(j).getIpAddress();
						j++;
					}
					sendData(oos, oosData);break;
				case "CentralRoute":
					for(int i = 0;i<connectedservers.size();i++)
						if(!connectedservers.get(i).getName().equals((String)oisData[1]))
							sendData(connectedservers.get(i).getOos(), new Object[]{"Route",oisData[2],oisData[3],oisData[4]});break;//sender receiver message
				default:
					//for(int i = 0;i<oisData.length;i++ )
						//System.out.println(oisData[i]);
				}
			}
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	
	private void sendData(ObjectOutputStream oos , Object[] data){
    	try {
    		//System.out.println(data[0]);
    		oos.reset();
			oos.writeObject(data);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
	
	public static void main(String[] args) {
		new CentralServer(6000);
		
	}

}
