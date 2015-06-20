package ca.fiset.dirdup.copy;

import java.io.File;
import java.io.StringWriter;
import java.util.List;

public class DiskItem implements DirectoryItem {

	private File entry;
	private List<String> path;
	
	public DiskItem(File entry, List<String> path) {
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
	public File getEntry(){
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

	@Override
	public String getSourcePathAndName() {
		return getPathAndName();
	}
}
