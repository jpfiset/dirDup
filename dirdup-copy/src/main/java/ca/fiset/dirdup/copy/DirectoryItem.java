package ca.fiset.dirdup.copy;

import java.util.List;

import ca.carleton.gcrc.couch.fsentry.FSEntry;

public interface DirectoryItem {
	
	List<String> getPath();
	
	String getName();

	FSEntry getEntry();
	
	String getPathAndName();
}
