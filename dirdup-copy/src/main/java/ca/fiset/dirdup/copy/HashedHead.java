package ca.fiset.dirdup.copy;

import java.util.List;
import java.util.Vector;

public class HashedHead implements DirectoryHead {

	private List<DirectoryItem> items = new Vector<DirectoryItem>();
	
	public HashedHead(DirectoryHead srcHead) throws Exception {
		List<DirectoryItem> items = srcHead.getItems();
		for(DirectoryItem item : items){
			HashedItem targetItem = new HashedItem(item);
			this.items.add(targetItem);
		}
	}
	
	@Override
	public List<DirectoryItem> getItems() {
		return this.items;
	}
}
