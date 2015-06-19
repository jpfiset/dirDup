package ca.fiset.dirdup.copy;

import java.io.File;

public class DirectoryItemUtils {
	
	static public File getFile(File dir, DirectoryItem item){
		File file = dir;
		
		for(String frag : item.getPath()){
			file = new File(file, frag);
		}
		
		file = new File(file, item.getName());
		
		return file;
	}
	
}
