package ca.fiset.dirdup.copy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class HeadComparator {

//	private DirectoryHead sourceHead;
//	private DirectoryHead targetHead;
	private List<DirectoryItem> addedItems = new Vector<DirectoryItem>();
	private List<DirectoryItem> deletedItems = new Vector<DirectoryItem>();
	
	public HeadComparator(DirectoryHead sourceHead, DirectoryHead targetHead){
//		this.sourceHead = sourceHead;
//		this.targetHead = targetHead;
		
		// Accumulate source items
		Map<String,DirectoryItem> sourceItems = new HashMap<String,DirectoryItem>();
		for(DirectoryItem item : sourceHead.getItems()){
			sourceItems.put(item.getPathAndName(), item);
		}
		
		// Accumulate target items
		Map<String,DirectoryItem> targetItems = new HashMap<String,DirectoryItem>();
		for(DirectoryItem item : targetHead.getItems()){
			targetItems.put(item.getPathAndName(), item);
		}
		
		// Figure out the added items
		for(String fullPath : sourceItems.keySet()){
			DirectoryItem sourceItem = sourceItems.get(fullPath);
			DirectoryItem targetItem = targetItems.get(fullPath);
			if( null == targetItem ){
				// In source items but not in target items. Added
				addedItems.add( sourceItem );
			}
		}
		
		// Figure out the deleted items
		for(String fullPath : targetItems.keySet()){
			if( null == sourceItems.get(fullPath) ){
				// In target item but not in source items. Deleted
				deletedItems.add( targetItems.get(fullPath) );
			}
		}
	}
	
	public List<DirectoryItem> getAddedItems(){
		return addedItems;
	}
	
	public List<DirectoryItem> getDeletedItems(){
		return deletedItems;
	}
}
