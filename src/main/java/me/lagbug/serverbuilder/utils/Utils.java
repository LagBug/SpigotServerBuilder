package me.lagbug.serverbuilder.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class Utils {

	public static void copyFile(File input, File output) {
		try {
			Files.copy(input.toPath(), output.toPath(), StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

}
