package ca.fiset.dirdup.copy;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.List;

import ca.carleton.gcrc.couch.fsentry.FSEntry;
import ca.carleton.gcrc.couch.fsentry.FSEntryFile;

public class HeadCopier {
	
	private PrintWriter pw = null;

	public PrintWriter getPrintWriter() {
		return pw;
	}

	public void setPrintWriter(PrintWriter pw) {
		this.pw = pw;
	}

	public void copyHead(DirectoryHead srcHead, File targetDir) throws Exception {
		// Get list of target files
		DiskHead targetHead = new DiskHead( new FSEntryFile(targetDir) );
		
		// Compare source and target
		HeadComparator comparator = new HeadComparator(srcHead, targetHead);
		
		// Delete items
		{
			List<DirectoryItem> itemsToDelete = comparator.getDeletedItems();
			for(DirectoryItem itemToDelete : itemsToDelete){
				File fileToDelete = DirectoryItemUtils.getFile(targetDir, itemToDelete);
				
				if( null != pw ){
					pw.println("Deleting "+fileToDelete.getAbsolutePath());
				}
				fileToDelete.delete();
			}
		}
		
		// Add items
		{
			List<DirectoryItem> itemsToAdd = comparator.getAddedItems();
			for(DirectoryItem itemToAdd : itemsToAdd){
				File fileToAdd = DirectoryItemUtils.getFile(targetDir, itemToAdd);
				
				if( null != pw ){
					pw.println("Copy: "+fileToAdd.getAbsolutePath());
				}
				
				copyFile(itemToAdd.getEntry(), fileToAdd);
			}
		}
	}

	private void copyFile(FSEntry entry, File fileToAdd) throws Exception {
		FileOutputStream fos = null;
		InputStream is = null;
		
		try {
			File parent = fileToAdd.getParentFile();
			if( false == parent.exists() ){
				parent.mkdirs();
			}
			
			fos = new FileOutputStream(fileToAdd);
			is = entry.getInputStream();

			int b = is.read();
			while( b >= 0 ){
				fos.write(b);
				b = is.read();
			}
			
			fos.flush();
			
		} catch(Exception e) {
			throw new Exception("Error copying file "+entry.getName(),e);
			
		} finally {
			try {
				if( null != fos ){
					fos.close();
				}
			} catch (Exception e) {
				// Ignore
			}
			try {
				if( null != is ){
					is.close();
				}
			} catch (Exception e) {
				// Ignore
			}
		}
	}
}
