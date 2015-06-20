package ca.fiset.dirdup.copy;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.nio.channels.FileChannel;
import java.util.List;

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
		DiskHead targetHead = new DiskHead( targetDir );
		
		// Compare source and target
		HeadComparator comparator = new HeadComparator(srcHead, targetHead);
		
		// Delete items
		{
			List<DirectoryItem> itemsToDelete = comparator.getDeletedItems();
			for(DirectoryItem itemToDelete : itemsToDelete){
				File fileToDelete = DirectoryItemUtils.getFile(targetDir, itemToDelete);
				
				if( null != pw ){
					pw.println("Deleting "+itemToDelete.getPathAndName());
					pw.flush();
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
					pw.println("Copy "+itemToAdd.getSourcePathAndName()+" -> "+itemToAdd.getPathAndName());
					pw.flush();
				}
				
				File parent = fileToAdd.getParentFile();
				if( false == parent.exists() ){
					parent.mkdirs();
				}
				
				File entry = itemToAdd.getEntry();
				nioTransferCopy(entry, fileToAdd);
			}
		}
	}
	
	private void nioTransferCopy(File source, File target) throws Exception {
		FileInputStream fis = null;
		FileOutputStream fos = null;
		FileChannel in = null;
        FileChannel out = null;

        try {
        	fis = new FileInputStream(source);
        	fos = new FileOutputStream(target);
        	
            in = fis.getChannel();
            out = fos.getChannel();

            long size = in.size();
            long transferred = in.transferTo(0, size, out);

            while(transferred != size){
                transferred += in.transferTo(transferred, size - transferred, out);
            }
        } catch (Exception e) {
            throw new Exception("Error copying file "+source.getPath()+" to "+target.getPath(),e);
        } finally {
	        try {
				if( null != in ){
					in.close();
				}
	        } catch (Exception e) {
	            // ignore
	        }
	        try {
				if( null != out ){
					out.close();
				}
	        } catch (Exception e) {
	            // ignore
	        }
	        try {
				if( null != fis ){
					fis.close();
				}
	        } catch (Exception e) {
	            // ignore
	        }
	        try {
				if( null != fos ){
					fos.close();
				}
	        } catch (Exception e) {
	            // ignore
	        }
        }
    }
}
