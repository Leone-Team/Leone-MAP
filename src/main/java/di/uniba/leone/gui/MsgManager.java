package di.uniba.leone.gui;

import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 *
 * @author giann
 */
public class MsgManager {

    private final JTextArea out;
    private final JTextField in;
    private final Object lock = new Object();
    private String message = null;

    /**
     *
     * @param out
     * @param in
     */
    public MsgManager(JTextArea out, JTextField in) {
        this.out = out;
        this.in = in;
    }

    /**
     *
     */
    public void enterMsg() {
        synchronized (lock) {
            message = in.getText();
            in.setText("");
            displayMsg(" ");
            lock.notify(); // Notify the waiting thread
        }

    }

    
    /** 
     * @param msg
     */
    public void displayMsg(String msg) {
        out.append(msg + "\n");
        out.setCaretPosition(out.getDocument().getLength());
    }

    
    /** 
     * @return String
     */
    public String getMsg() {
        synchronized (lock) {
            try {
                lock.wait();
            } catch (InterruptedException ex) {
                System.out.println("Enter premuto");
            }
        }
        return message;
    }



    
}
