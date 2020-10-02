package principal;

import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.StageStyle;
import org.opencv.core.Core;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXMLLoader;


public class Main extends Application {

	private static double xOffset = 0;
	private static double yOffset = 0;

	@Override
	public void start(Stage primaryStage) {
		System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
		try {
			BorderPane root = (BorderPane)FXMLLoader.load(getClass().getResource("Principal.fxml"));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.initStyle(StageStyle.UNDECORATED);
			primaryStage.getProperties().put("hostServices", this.getHostServices());
			primaryStage.getIcons().add(new Image("file:icon.png"));
			primaryStage.setTitle("Machine Learning Keygen v3.1 - Core");
			primaryStage.show();

			root.setOnMousePressed(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					xOffset = primaryStage.getX() - event.getScreenX();
					yOffset = primaryStage.getY() - event.getScreenY();
				}
			});

			root.setOnMouseDragged(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					primaryStage.setX(event.getScreenX() + xOffset);
					primaryStage.setY(event.getScreenY() + yOffset);
				}
			});

		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
