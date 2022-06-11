package me.lagbug.serverbuilder.utils;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.RoundingMode;
import java.net.URL;
import java.text.DecimalFormat;

import javafx.application.Platform;
import javafx.scene.control.Label;

public class Downloader {
	
	private String url, output;
	private double sumCount;
	private Label label;
	
	public Downloader(Label label, String url, String output) {
		this.label = label;
		this.url = url;
		this.output = output;
	}
	
	public Downloader download() {
		try {
			URL urlM = new URL(url);
			int size = urlM.openConnection().getContentLength();
			BufferedInputStream in = new BufferedInputStream(urlM.openStream());
	        FileOutputStream out = new FileOutputStream(output);
	        DecimalFormat df = new DecimalFormat("#.##");
	        df.setRoundingMode(RoundingMode.FLOOR);
	        
	        byte[] buffer = new byte[1024];
	        int count = 0;
	        sumCount = 0.00;
	        
	        while ((count = in.read(buffer, 0, 1024)) != -1) {
	            out.write(buffer, 0, count);
	            sumCount += count;
	            Platform.runLater(new Runnable() {
					@Override
					public void run() {
						if (label != null) label.setText(df.format(sumCount / size * 100) + "%");
					}
				});
	        }
	        
	        in.close();
	        out.close();
		} catch (IOException ex) {
			return null;
		}
		return this;
	}
}