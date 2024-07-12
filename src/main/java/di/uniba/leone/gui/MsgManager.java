
package di.uniba.leone.gui;

import javax.swing.JTextArea;
import javax.swing.JTextField;


public class MsgManager {

    private final JTextArea out;
    private final JTextField in;
    private final Object lock = new Object();
    private String message = null;

    public MsgManager(JTextArea out, JTextField in) {
        this.out = out;
        this.in = in;
    }

    public void enterMsg() {
        synchronized (lock) {
            message = in.getText();
            in.setText("");
            displayMsg(" ");
            lock.notify(); // Notify the waiting thread
        }

    }

    public void displayMsg(String msg) {
        out.append(msg + "\n");
        out.setCaretPosition(out.getDocument().getLength());
    }

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
