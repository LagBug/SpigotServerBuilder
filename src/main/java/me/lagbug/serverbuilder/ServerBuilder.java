package me.lagbug.serverbuilder;

import java.io.File;
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ServerBuilder extends Application {

	private double xOffset = 0;
	private double yOffset = 0;

	private Stage stage;
	private Scene scene;
	private File output;
	private static ServerBuilder instance;
	private String version;
	public final static String VERSION = "0.2";

	@Override
	public void start(Stage primaryStage) throws IOException {
		instance = this;
		stage = primaryStage;
		
		Parent root = FXMLLoader.load(getClass().getResource("/fxml/Builder.fxml"));
		root.setOnMousePressed(event -> { xOffset = event.getSceneX(); yOffset = event.getSceneY(); });
		root.setOnMouseDragged(event -> { stage.setX(event.getScreenX() - xOffset); stage.setY(event.getScreenY() - yOffset); });
		
		scene = new Scene(root);
		scene.getStylesheets().addAll(getClass().getResource("/css/style.css").toExternalForm());
		
		stage.getIcons().add(new Image("icon.png"));
		stage.setScene(scene);
		stage.setResizable(false);	
		stage.initStyle(StageStyle.TRANSPARENT);
		stage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}

	public void switchStage(String fxmlName) {
	    Parent root = null;
		try {
			root = FXMLLoader.load(getClass().getResource("/fxml/" + fxmlName));
			root.setOnMousePressed(event -> { xOffset = event.getSceneX(); yOffset = event.getSceneY(); });
			root.setOnMouseDragged(event -> { stage.setX(event.getScreenX() - xOffset); stage.setY(event.getScreenY() - yOffset); });
			
			scene.setRoot(root);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	public Stage getStage() {
		return this.stage;
	}
	
	public static ServerBuilder getInstance() {
		return instance;
	}
	
	public File getDirectory() {
		return output;
	}
	
	public void setDirecotry(File file) {
		this.output = file;
	}
	
	public String getVersion() {
		return this.version;
	}
	
	public void setVersion(String version) {
		this.version = version;
	}
}
