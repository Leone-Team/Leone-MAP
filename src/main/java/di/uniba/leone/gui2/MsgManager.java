
package di.uniba.leone.gui2;

import javax.swing.JTextField;
import javax.swing.JTextPane;


public class MsgManager {
    private final JTextPane out;
    private final JTextField in;
    public MsgManager(JTextPane out, JTextField in) {
        this.out = out;
        this.in = in;
    }
    
    public String enterMsg(){
        String message = in.getText();
        displayMsg(message);
        in.setText("");
        return message;
    }
    
    public void displayMsg(String message){
        out.setText(out.getText() + "\n" + message);
    }
    
}
