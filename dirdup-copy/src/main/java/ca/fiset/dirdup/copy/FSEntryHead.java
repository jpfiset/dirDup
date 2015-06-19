package ca.fiset.dirdup.copy;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.Vector;

import ca.carleton.gcrc.couch.fsentry.FSEntry;

public class FSEntryHead implements DirectoryHead {

	private FSEntry head;
	
	public FSEntryHead(FSEntry head){
		this.head = head;
	}

	@Override
	public List<DirectoryItem> getItems(){
		List<DirectoryItem> result = new Vector<DirectoryItem>();
		Stack<String> path = new Stack<String>();
		getItems(head, path, result);
		return result;
	}

	public void getItems(FSEntry dir, Stack<String> path, List<DirectoryItem> items){
		List<String> effectivePath = new ArrayList<String>(path);
		
		List<FSEntry> children = dir.getChildren();
		for(FSEntry child : children){
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
