package firesa.com.br.corretorDeXml;


import java.net.URL;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

@SpringBootApplication
public class InitApp extends Application {

	@Override
	public void start(Stage stage) throws Exception {
		URL url = getClass().getResource("TelaCorrigirXml.fxml");
		Parent root = FXMLLoader.load(url);
		Scene scene = new Scene(root);
       
        stage.centerOnScreen();
        stage.setTitle("Corretor de Erro Protocolo");
        stage.setResizable(false);
        stage.initStyle(StageStyle.DECORATED);
        stage.setScene(scene);
        stage.show();
    }

	public static void main(String[] args) {
		launch(args);
	}

}
