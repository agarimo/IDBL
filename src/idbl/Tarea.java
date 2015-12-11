package idbl;

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.concurrent.Task;

/**
 *
 * @author Agarimo
 */
public class Tarea extends Task{
    
    
    public Tarea(){
        
    }

    @Override
    protected Void call() {
        for (int i = 0; i < 10000; i++) {
            try {
                updateProgress(i, 10000);
                updateMessage("Iteracion "+i);
                Thread.sleep(10);
            } catch (InterruptedException ex) {
                Logger.getLogger(Tarea.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }
}
