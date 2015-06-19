package ca.fiset.dirdup.copy;

import java.util.List;
import java.util.Vector;

public class CollectionHead implements DirectoryHead {

	private List<DirectoryItem> items = new Vector<DirectoryItem>();
	
	@Override
	public List<DirectoryItem> getItems() {
		return items;
	}

	public void addItem(DirectoryItem item){
		items.add(item);
	}
}
