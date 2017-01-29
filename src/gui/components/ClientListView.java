package gui.components;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;

import javax.swing.*;

import gui.Client;

public class ClientListView extends View{

	Font font;
	private JPanel jp;
	private View view;
	private TableModel tableModel;
	public ClientListView(Client view , String name, JTextArea textArea,JTextField textField ,ObjectOutputStream oos, ObjectOutputStream oosCentral){
		//super(new ImageIcon("BG.png"));
		setTitle("Abrakadabra Messenger");
		setSize(400, 600);
		setLayout(new GridLayout());
		this.view =view;
		this.tableModel = view.getTableList();
		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.setBounds(0, 50, 400, 500);

		try {
		    font = Font.createFont(Font.TRUETYPE_FONT, new File("Roboto-Bold.ttf")).deriveFont(30f);
		    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		    ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("Roboto-Bold.ttf")));
		} catch (IOException e) {
		    e.printStackTrace();
		} catch(FontFormatException e) {
		    e.printStackTrace();
		}
		
		JLabel t = new JLabel("Online Users List");
		t.setFont(font);
		t.setForeground(Color.WHITE);
		t.setAlignmentX(Component.CENTER_ALIGNMENT);
		t.setBounds(50, 10, 400, 50);
		JLabel background=new JLabel(new ImageIcon("BG.png"));
        add(background);
        background.setLayout(null);
        
		jp = new JPanel();
		jp.setBounds(0, 100, 400, 590);
		jp.setLayout(new GridLayout(6, 1, 5, 5));
		jp.setOpaque(false);
		
		JPanel title = new JPanel();
		title.setOpaque(false);
		
		//refresh.setFont(font);
		//refresh.setForeground(Color.WHITE);
		JButton refresh = new JButton("Refresh");
		refresh.setAlignmentX(Component.RIGHT_ALIGNMENT);
		refresh.setBounds(300, 25, 100, 30);
		background.add(refresh, BorderLayout.NORTH);
		
//		JLabel upperPart = new JLabel();
//		upperPart.setPreferredSize(new Dimension(300, 0));
//		
//		upperPart.add(t);
//		title.add(upperPart);
		//add(title, BorderLayout.NORTH);
		
		setLocationRelativeTo(view);
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);	
		
		
		
//		OnlineClientLabel ol = new OnlineClientLabel();
//		//ol.setBounds(0, 70, 400, 500);
////		ol.addUser("Mark");
////		ol.addUser("Ayman");
//		
//		OnlineClientLabel ol2 = new OnlineClientLabel();
////		ol2.addUser("Rodaina");
////		ol2.addUser("Mariem");
//		
//		OnlineClientLabel ol3 = new OnlineClientLabel();
//		
//		OnlineClientLabel ol4 = new OnlineClientLabel();
//		
//		OnlineClientLabel ol5 = new OnlineClientLabel();
		
		//allmembersss
		refresh.addActionListener((ActionEvent e)->{
			new Thread(() -> reload(tabbedPane,name, textArea, textField, oos, oosCentral)).start();
		});
		refresh.doClick();
//		tabbedPane.add("Server 1", ol2);
//		tabbedPane.add("Server 2", ol3);
//		tabbedPane.add("Server 3", ol4);
//		tabbedPane.add("Server 4", ol5);

		
		title.add(t);
		background.add(tabbedPane);
		background.add(title, BorderLayout.NORTH);
//		background.add(jp);
		background.add(t);
	}


	private void reload(JTabbedPane tabbedPane, String name, JTextArea textArea, JTextField textField ,ObjectOutputStream oos , ObjectOutputStream oosCentral) {
		try {
			for(int i=0;i<tableModel.getList().size() ;i++){
			Thread.sleep(1);
			System.out.println(tableModel.getList().get(i).getClients().size());
			sendData(oosCentral, new Object[]{"ServerGetClients",tableModel.getList().get(i).getName()});
			System.out.println(tableModel.getList().get(i).getClients().size());
			}
			tabbedPane.removeAll();
			OnlineClientLabel ols[]= new OnlineClientLabel[tableModel.getList().size()+1];
			tabbedPane.add("All Members", ols[0]=new OnlineClientLabel());
			
			for(int i=1 ;i<ols.length;i++){
				tabbedPane.add(tableModel.getList().get(i-1).getName(), ols[i]=new OnlineClientLabel());
			for(int j=0;j<tableModel.getList().get(i-1).getClients().size();j++)
				if(!tableModel.getList().get(i-1).getClients().get(j).equals(name)){
				ols[0].addUser(tableModel.getList().get(i-1).getClients().get(j));
				ols[i].addUser(tableModel.getList().get(i-1).getClients().get(j));
				
				}
			}
			for(int i = 0;i<ols.length;i++)
				for(int j =0;j<ols[i].getuBox().getComponentCount();j++)
					if(ols[i].getuBox().getComponent(j) instanceof JButton){
						JButton btn = ((JButton) ols[i].getuBox().getComponent(j));
						btn.addActionListener((ActionEvent e) ->{ new ChatView(btn.getText(), textArea, textField, oos).setVisible(true); });
						}
			tabbedPane.revalidate();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}


	public void addUsers(){
		
	}
	public static void main(String[] args) {
//		new ClientListView(null).setVisible(true);
	}
}
