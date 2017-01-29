package gui.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;




public class OnlineClientLabel extends JScrollPane{
    private ArrayList<String> uNames;
    private JPanel uBox;
	private Font font;
	public OnlineClientLabel(){
		super();
		
		try {
		    font = Font.createFont(Font.TRUETYPE_FONT, new File("Roboto-Bold.ttf")).deriveFont(20f);
		    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		    ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("Roboto-Bold.ttf")));
		} catch (IOException e) {
		    e.printStackTrace();
		} catch(FontFormatException e) {
		    e.printStackTrace();
		}
		
		uBox = new JPanel();
		//uBox.setBackground(Color.black);
		uBox.setLayout(new FlowLayout());
		uBox.setPreferredSize(new Dimension(400, 500));
		uBox.setOpaque(false);
		
		setViewportView(uBox);
		
		getViewport().setOpaque(false);
		setBorder(BorderFactory.createEmptyBorder());
		setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		
		uNames = new ArrayList<String>();
		setOpaque(false);
		
//		addUser("Rodaina");
//		addUser("Mark");
//		addUser("Ayman");
	}
	
	   public void addUser(String name) {
			JButton userButton = new JButton(new ImageIcon("user2.png"));
			userButton.setBorder(BorderFactory.createEmptyBorder());
			userButton.setText(name);
			//userButton.setVerticalTextPosition(SwingConstants.TOP);
			userButton.setHorizontalTextPosition(SwingConstants.CENTER);
			//userButton.setIcon();
			userButton.setRolloverIcon(new ImageIcon("user1.png"));
			uBox.add(userButton);
			uNames.add(name);
			userButton.setPreferredSize(new Dimension(400, 45));
			userButton.setOpaque(false);
			userButton.setContentAreaFilled(false);
			userButton.setForeground(Color.WHITE);
			userButton.setFont(font);
			
			
		    }
	   public JPanel getuBox() {
			return uBox;
		}
	
	
	
}
