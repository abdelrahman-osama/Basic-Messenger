package gui.components;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.URL;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public abstract class View extends JFrame {
	private static final long serialVersionUID = 1L;
	
	public void setupDisplay(int width , int height, JLabel backgroundIMG) {
		setResizable(false);
		setUndecorated(true);
		setSize(width,height);
		setLocationRelativeTo(null);
		setContentPane(backgroundIMG);
		setLayout(new BorderLayout());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public URL loadResource(String resourceLocation){
		URL url = getClass().getClassLoader().getResource(resourceLocation);
		if(url==null)
			System.out.println(resourceLocation + " Unable to locate Resource");
		//	new User_Notify(null, 'E',"Unable To locate Resource");
		return url;
	}
	public void showMessage(String msg){
		JOptionPane.showMessageDialog(this,msg);
	}
	
	protected void sendData(ObjectOutputStream oos , Object[] data){
    	try {
    		oos.reset();
			oos.writeObject(data);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
	
	public void addComponent(JPanel container, Component component, int gridx, int gridy,
		      int gridwidth, int gridheight, double weightx , double weighty ,int anchor, 
		      int fill, int insectTop,int insectLeft,int insectBottom, int insectRight,int ipadx, int ipady) {//s7r el GridbagLayout XD
		    GridBagConstraints gbc = new GridBagConstraints(gridx, gridy, gridwidth, gridheight,weightx, weighty,
		        anchor, fill, new Insets(insectTop, insectLeft, insectBottom, insectRight), ipadx, ipady);
		    container.add(component, gbc);
		  }

}
