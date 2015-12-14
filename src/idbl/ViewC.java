package idbl;

import insert.Insercion;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

/**
 *
 * @author Agarimo
 */
public class ViewC implements Initializable {

    @FXML
    private Label title;
    @FXML
    private Label label;
    @FXML
    private ProgressBar progreso;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        run();
    }

    public void run() {
        Insercion a = new Insercion();
        progreso.progressProperty().bind(a.progressProperty());
        label.textProperty().bind(a.messageProperty());
        title.textProperty().bind(a.titleProperty());
        new Thread(a).start();
    }
}
