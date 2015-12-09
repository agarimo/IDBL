package insrt;

/**
 *
 * @author Agarimo
 */
public class Insercion extends Thread {
    InsercionC i;
    
    
    public Insercion(){
        i=new InsercionC();
    }
    
    @Override
    public void run(){
        i.start();
    }
}
