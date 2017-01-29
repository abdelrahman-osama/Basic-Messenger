package gui.components;

import javax.swing.JTextArea;
import javax.swing.text.DefaultCaret;

public class ServerConsole extends JTextArea{
	private DefaultCaret caret;
	private boolean Error_Reached = false;
	private static final long serialVersionUID = 1L;
	public ServerConsole(){
		caret = (DefaultCaret)getCaret();
	}
	public void clearConsole(){
		setText(null);
		Error_Reached = false;
	}
	public void clearError(){
		Error_Reached = false;
	}
	public void appendConsole(String message){
		if(message.contains("ERROR"))
			Error_Reached=true;
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		append(message + "\n");
	}
	public boolean isError_Reached() {
		return Error_Reached;
	}
}
