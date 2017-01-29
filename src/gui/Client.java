package gui;
import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Array;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import engine.components.ServerTable;
import gui.components.ChatView;
import gui.components.ClientListView;
import gui.components.TableModel;
import gui.components.View;

public class Client extends View {

	private TableModel tableList;
	private Socket centralSocket,socket;
	private ObjectOutputStream oosCentral;
	private static final long serialVersionUID = 1L;
	public Client(){
		tableList = new TableModel();
		setupDisplay(500,500,new JLabel(new ImageIcon(loadResource("resource/images/BG.png"))));
		JButton signin = new JButton("Sign in");
		JButton exit = new JButton("Exit");
		JPanel top = new JPanel(new GridBagLayout());
		JPanel center = new JPanel(new GridBagLayout());
		JPanel buttom = new JPanel(new GridBagLayout());
		JTextField name = new JTextField();
		exit.addActionListener((ActionEvent e) ->{
			closeOperation();
		});
		signin.addActionListener((ActionEvent e) ->{
			new Thread(() -> serverConnect(name.getText())).start();
		});
		JScrollPane table_scrollPane = new JScrollPane(tableList,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		tableList.addColumn(new String[]{"S"});
		top_Panel_Setup(top,exit);
		center_Panel_Setup(center,table_scrollPane);
		buttom_Panel_Setup(buttom,signin,name);
		add(top,BorderLayout.NORTH);
		add(center,BorderLayout.CENTER);
		add(buttom,BorderLayout.SOUTH);
		new Thread(() -> centralConnection("127.0.0.1",6000)).start();
	}
	
	public TableModel getTableList() {
		return tableList;
	}

	private void serverConnect(String name) {
		ObjectInputStream ois;
		ObjectOutputStream oos;
		Object oisData[],oosData[];
		JTextArea textArea = new JTextArea();
		JTextField textField = new JTextField();
		Socket socket;
		ArrayList<String> memberList= new ArrayList<String>();
		try {
			socket = new Socket(tableList.getList().get(tableList.getSelectedRow()).getIpAddress(), tableList.getList().get(tableList.getSelectedRow()).getPortno());
			oos = new ObjectOutputStream(socket.getOutputStream());
			ois = new ObjectInputStream(socket.getInputStream());
			sendData(oos, oosData = new Object[]{"ClientName",name});
			while(true){
				oisData = (Object[]) ois.readObject();
				switch((String)oisData[0]){
				case "ConnectedClientsList":
					memberList.clear();
					for(int i = 1 ;i <oisData.length ;i++)
						memberList.add((String) oisData[i]);
					break;
				case "Accepted":
					new Thread(() -> clientList(name,textArea, textField, oos)).start();
					break;
				case "Error":
					showMessage((String)oisData[1]);
				case "Message":
					textArea.append((String)oisData[1]+'\n');break;
				}
			}
			
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void clientList(String name,JTextArea textArea, JTextField textField, ObjectOutputStream oos) {
				new ClientListView(this,name, textArea, textField, oos ,oosCentral).setVisible(true);;
	}

	private void closeOperation() {
		try {
			dispose();
			if (centralSocket != null)
				centralSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}

	private void top_Panel_Setup(JPanel top, JButton exit) {
		top.setOpaque(false);
		exit.setOpaque(false);
		addComponent(top , exit , 0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, 0, 0, 0, 0, 0, 0);
	}
	
	private void center_Panel_Setup(JPanel center, JScrollPane table_scrollPane) {
		center.setOpaque(false);
		table_scrollPane.setBorder(null);
		table_scrollPane.setOpaque(false);
		table_scrollPane.getViewport().setOpaque(false);
		addComponent(center , table_scrollPane , 0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, 0, 0, 0, 0, 0, 0);
	}
	
	private void buttom_Panel_Setup(JPanel buttom, JButton signin, JTextField name) {
		Font font;
		buttom.setOpaque(false);
		JLabel namelbl = new JLabel("Name:");
		try {
			font = Font.createFont(Font.TRUETYPE_FONT, new File(loadResource("resource/font/Roboto-Bold.ttf").getFile())).deriveFont(22f);
			namelbl.setFont(font);
			signin.setFont(font);
			name.setFont(font);
		} catch (FontFormatException | IOException e) {
			e.printStackTrace();
		}
		addComponent(buttom , namelbl , 0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.VERTICAL, 10, 0, 10, 0, 10, 0);
		addComponent(buttom , name , 1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.VERTICAL, 10, 0, 10, 0, 150, 10);
		addComponent(buttom , signin , 2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.PAGE_END, GridBagConstraints.BOTH, 10, 10, 10, 0, 10, 10);
	}
	
	private void centralConnection(String ip, int port) {
		ObjectInputStream ois;
		Object oisData[],oosData[];
		try {
			centralSocket = new Socket(ip, port);
			ois = new ObjectInputStream(centralSocket.getInputStream());
			oosCentral = new ObjectOutputStream(centralSocket.getOutputStream());
			sendData(oosCentral, oosData = new Object[]{"SERVERSLIST"});
			while(true){
					oisData = (Object[]) ois.readObject();
				switch(((String) oisData[0])){
				case "SERVERS":
					tableList.clear();
					for(int i = 1;i<oisData.length;i+=3){
						tableList.addRow(new ServerTable((String)oisData[i], (Integer)oisData[i+1],(String)oisData[i+2]));
					}
					for(int i=0;i<tableList.getList().size() ;i++){
						sendData(oosCentral, new Object[]{"ServerGetClients",tableList.getList().get(i).getName()});
						Thread.sleep(250);
					}break;
						
				case "ServerClients":
					for(int i=0;i<tableList.getList().size();i++)
						if(((String)oisData[1]).equals(tableList.getList().get(i).getName())){
							tableList.getList().get(i).getClients().clear();
							for(int j=2;j<oisData.length;j++){
								tableList.getList().get(i).getClients().add((String) oisData[j]);
							}
						}
				}
			}
		} catch (IOException | ClassNotFoundException | InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		new Client().setVisible(true);
	}

}
