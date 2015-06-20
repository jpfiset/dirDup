package ca.fiset.dirdup.copy;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.Vector;

public class DiskHead implements DirectoryHead {

	private File head;
	
	public DiskHead(File head){
		this.head = head;
	}

	@Override
	public List<DirectoryItem> getItems(){
		List<DirectoryItem> result = new Vector<DirectoryItem>();
		Stack<String> path = new Stack<String>();
		getItems(head, path, result);
		return result;
	}

	public void getItems(File dir, Stack<String> path, List<DirectoryItem> items){
		List<String> effectivePath = new ArrayList<String>(path);
		
		String[] children = dir.list();
		for(String childName : children){
			File child = new File(dir,childName);
			if( child.isDirectory() ){
				path.push(child.getName());
				getItems(child, path, items);
				path.pop();
				
			} else if( child.isFile() ) {
				DiskItem item = new DiskItem(child, effectivePath);
				items.add(item);
			}
		}
	}
}
