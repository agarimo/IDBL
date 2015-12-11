package idbl;

import insert.Fichero;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

/**
 *
 * @author Agarimo
 */
public class ViewC implements Initializable {

    @FXML
    private Label label;
    @FXML
    private ProgressBar progreso;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        test1();
    }

    public void test() {

        Thread a = new Thread(() -> {

            Platform.runLater(() -> {

            });

            Platform.runLater(() -> {

            });
        });
        a.start();
    }

    public void test1() {
        Tarea a = new Tarea();
        progreso.progressProperty().bind(a.progressProperty());
        label.textProperty().bind(a.messageProperty());
        new Thread(a).start();
    }

}
