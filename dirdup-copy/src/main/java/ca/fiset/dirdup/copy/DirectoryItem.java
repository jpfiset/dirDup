package ca.fiset.dirdup.copy;

import java.io.File;
import java.util.List;

public interface DirectoryItem {
	
	List<String> getPath();
	
	String getName();

	File getEntry();

	String getPathAndName();

	String getSourcePathAndName();
}
