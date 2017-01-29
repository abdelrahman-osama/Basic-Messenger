package gui.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ChatView extends View implements KeyListener{
	private JTextArea textArea;
	private JTextField textField;
	private ObjectOutputStream oos;
	private static final long serialVersionUID = 1L;
	public ChatView(String name,JTextArea textArea ,JTextField textField, ObjectOutputStream oos){
		this.oos=oos;
		this.textArea=textArea;
		this.textField=textField;
		setTitle(name);
		setupDisplay(500, 500, new JLabel());
		setUndecorated(false);
		textArea.setEditable(false);
		textField.addKeyListener(this);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		textField.setPreferredSize(new Dimension(500, 50));
		textArea.setBackground(Color.PINK);
		add(textArea, BorderLayout.CENTER);
		add(textField, BorderLayout.SOUTH);
	}
	public static void main(String[] args) {
	}
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_ENTER){
			sendData(oos, new Object[]{"Chat",getTitle(),textField.getText()});
		textField.setText(null);
		}
	}
	public void keyReleased(KeyEvent arg0) {
		
	}
	public void keyTyped(KeyEvent arg0) {
		
	}
	public JTextArea getTextArea() {
		return textArea;
	}
	
}