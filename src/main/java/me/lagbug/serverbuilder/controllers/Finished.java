package me.lagbug.serverbuilder.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import me.lagbug.serverbuilder.ServerBuilder;

public class Finished {

	private final ServerBuilder sb = ServerBuilder.getInstance();

    @FXML
    private ImageView backgroundImage, closeImage;
    
	@FXML
	public void initialize() {
		backgroundImage.setImage(new Image("background.jpg"));
		closeImage.setImage(new Image("cancel.png"));
	}
	
    @FXML
    void onCreate(ActionEvent event) {
    	sb.switchStage("Builder.fxml");
    }

	@FXML
	void onClose(ActionEvent e) {
		sb.getStage().close();
		System.exit(0);
	}

}
