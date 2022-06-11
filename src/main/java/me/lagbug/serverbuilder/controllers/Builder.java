package me.lagbug.serverbuilder.controllers;

import java.awt.AWTException;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.jsoup.Jsoup;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXProgressBar;
import com.jfoenix.controls.JFXTextField;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.DirectoryChooser;
import me.lagbug.serverbuilder.ServerBuilder;
import me.lagbug.serverbuilder.utils.Downloader;
import me.lagbug.serverbuilder.utils.Utils;

public class Builder {

	private final ServerBuilder sb = ServerBuilder.getInstance();
	private List<String> messages;

	@FXML
	private JFXButton startBtn;

	@FXML
	private JFXTextField versionField;

	@FXML
	private VBox chatVbox;

	@FXML
	private Pane downloadingPane;

	@FXML
	private JFXProgressBar progressBar;

	@FXML
	private Label infoLabel;

	@FXML
	private ImageView closeImage;
	
	@FXML
	public void initialize() throws AWTException, IOException {
		messages = new ArrayList<>();
		chatVbox.setSpacing(3.7);
		addLabel("Started ServerBuilder version [" + ServerBuilder.VERSION + "]");

		closeImage.setImage(new Image("cancel.png"));
		
		Scanner sc = new Scanner(new URL("https://hub.spigotmc.org/versions/").openStream());
		while (sc.hasNextLine()) {
			String version = Jsoup.parse(sc.nextLine()).text().split(".json")[0];
			if (!version.isEmpty() && !version.contains("Index") && version.contains(".")) {
					//versions.add(version);
			}
		}

		sc.close();

	}

	@FXML
	void onBuild(ActionEvent e) throws IOException {
		if (chatVbox.getChildren().size() > 12) {
			messages.clear();
			chatVbox.getChildren().clear();
		}

		if (startBtn.getText().equalsIgnoreCase("continue")) {
			
			File[] dir = new File(sb.getDirectory().getPath() + File.separator + "buildtools").listFiles();
			File toUse = null;
			
			for (File f : dir) {
				if (f.getName().startsWith("spigot") && f.isFile()) {
					toUse = f;
					break;
				}
			}
					
			Utils.copyFile(toUse, new File(sb.getDirectory().getPath() + File.separator + "spigot" + File.separator + toUse.getName()));
			sb.switchStage("Eula.fxml");
			return;
		}
		
		
		if (versionField.getText().isEmpty()) {
			addLabel("No version has been selected.");
			return;
		}

		sb.setVersion(versionField.getText());

		DirectoryChooser dirChooser = new DirectoryChooser();
		dirChooser.setTitle("Choose output folder");
		File dirChoosen = dirChooser.showDialog(sb.getStage());

		if (dirChoosen == null) {
			addLabel("Cancelled choosing output folder.");
			return;
		}

		sb.setDirecotry(dirChoosen);
		progressBar.setVisible(false);
		progressBar.setVisible(true);
		downloadingPane.setVisible(true);
		startBtn.setDisable(true);

		addLabel("Downloading official spigot buildtools.");
		new File(sb.getDirectory().getPath() + File.separator + "buildtools").mkdirs();
		new File(sb.getDirectory().getPath() + File.separator + "spigot").mkdirs();

		new Thread(new Runnable() {
			@Override
			public void run() {
				new Downloader(infoLabel,
						"https://hub.spigotmc.org/jenkins/job/BuildTools/lastSuccessfulBuild/artifact/target/BuildTools.jar",
						sb.getDirectory().getPath() + File.separator+ "buildtools" + File.separator + "BuildTools.jar").download();
				
				Platform.runLater(() -> {
					addLabel("Successfully downloaded build tools.");
					addLabel("Creating custom winodws batch file to run buildtools.");
					File run = new File(sb.getDirectory() + File.separator + "buildtools" + File.separator + "startBuildtools.bat");
					PrintWriter pw = null;
					
					try {
						run.createNewFile();
						pw = new PrintWriter(run);
					} catch (IOException e) {
						e.printStackTrace();
					}

					pw.write("@echo off\r\n" + "set startdir=%~dp0\r\n"
							+ "set bashdir=\"C:\\Program Files\\Git\\bin\\bash.exe\"\r\n"
							+ "%bashdir% --login -i -c \"java -jar \"\"%startdir%\\BuildTools.jar\"\"\" --rev " + sb.getVersion() + "\r\n"
							+ "pause");
					pw.close();

					ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/C", "Start", "startBuildtools.bat");
					
					pb.directory(run.getParentFile());
					try {
						pb.start();
					} catch (IOException e) {
						e.printStackTrace();
					}
					
					addLabel("Created batch file with success");
					addLabel("Now running buildtools.");
					addLabel("Click continue once it has finished isntalling.");
					
					startBtn.setText("Continue");
					startBtn.setDisable(false);
					downloadingPane.setVisible(false);
				});
			}
		}).start();
	}


	@FXML
	void onClose(ActionEvent e) {
		sb.getStage().close();
		System.exit(0);
	}

	private void addLabel(String text) {
		if (!messages.isEmpty() && messages.get(messages.size() - 1).equals(text)) {
			return;
		}

		Label l = new Label(text);
		l.setFont(Font.font("Microsoft YaHei UI Light", FontWeight.EXTRA_LIGHT, 9));
		chatVbox.getChildren().add(l);
		messages.add(l.getText());
	}

}
