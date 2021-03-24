package isa.util;

import java.io.File;

public class IOUtil {

	public static void deleteDirContent(String path) {
		File directory = new File(path);
		File[] allContents = directory.listFiles();
		if (allContents != null) {
			for (File file : allContents) {
				file.delete();
			}
		}
	}

}
