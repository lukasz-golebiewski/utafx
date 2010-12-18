package utafx.data;

import java.io.File;
import java.io.IOException;

public class FileUtil {
    public static void delete(String path) throws IOException {
	File f = new File(path);
	if (f.exists()) {
	    boolean success = f.delete();
	    if (!success) {
		throw new IOException("Could not remove file " + path);
	    }
	}
    }

    public static void createDirectory(String dir) {
	new File(dir).mkdirs();	
    }
}
