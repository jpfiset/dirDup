package ca.fiset.dirdup.copy;

import java.io.StringWriter;
import java.util.List;

import ca.carleton.gcrc.couch.fsentry.FSEntry;

public class DiskItem implements DirectoryItem {

	private FSEntry entry;
	private List<String> path;
	
	public DiskItem(FSEntry entry, List<String> path) {
		this.entry = entry;
		this.path = path;
	}
	
	@Override
	public List<String> getPath(){
		return path;
	}
	
	@Override
	public String getName(){
		return entry.getName();
	}

	@Override
	public FSEntry getEntry(){
		return entry;
	}
	
	@Override
	public String getPathAndName(){
		StringWriter sw = new StringWriter();
		
		for(String fragPath : getPath()){
			sw.write(fragPath);
			sw.write("/");
		}
		
		sw.write(getName());
		
		sw.flush();
		
		return sw.toString();
	}
}
