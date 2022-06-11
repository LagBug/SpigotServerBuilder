package me.lagbug.serverbuilder.controllers;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import me.lagbug.serverbuilder.ServerBuilder;

public class Eula {

	private final ServerBuilder sb = ServerBuilder.getInstance();

    @FXML
    private ImageView backgroundImage, closeImage;
	
	@FXML
	public void initialize() {
		backgroundImage.setImage(new Image("background.jpg"));
		closeImage.setImage(new Image("cancel.png"));
	}
    
    @FXML
    void onAccept(ActionEvent event) throws IOException {
    	runBat(true);
    }

    @FXML
    void onDeny(ActionEvent event) throws IOException {
    	runBat(false);
    }
    
    @FXML
    void onClose(ActionEvent event) {
		sb.getStage().close();
		System.exit(0);
    }
    
    private void runBat(boolean accepted) throws IOException {
    	File eula = new File(sb.getDirectory() + File.separator + "spigot" + File.separator + "eula.txt"), run = new File(sb.getDirectory() + File.separator + "spigot" + File.separator + "run.bat");
    	PrintWriter pw = new PrintWriter(eula);
    	
    	eula.createNewFile(); run.createNewFile();
		pw.write("eula=" + accepted); pw.close();
    	
		pw = new PrintWriter(run);
		pw.write("java -jar spigot-" + sb.getVersion() + ".jar"); pw.close();
    	
		ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/C", "Start","run.bat");
	    pb.directory(run.getParentFile());
		pb.start();	
		
		sb.switchStage("Finished.fxml");
    }

}
