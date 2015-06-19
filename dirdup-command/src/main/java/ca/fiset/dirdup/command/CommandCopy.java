package ca.fiset.dirdup.command;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Stack;

import ca.carleton.gcrc.couch.fsentry.FSEntry;
import ca.carleton.gcrc.couch.fsentry.FSEntryFile;
import ca.fiset.dirdup.copy.CollectionHead;
import ca.fiset.dirdup.copy.DirectoryItem;
import ca.fiset.dirdup.copy.DirectoryItemUtils;
import ca.fiset.dirdup.copy.FSEntryHead;
import ca.fiset.dirdup.copy.HashedItem;
import ca.fiset.dirdup.copy.HeadComparator;

public class CommandCopy implements Command {

	@Override
	public String getCommandString() {
		return "copy";
	}

	@Override
	public boolean matchesKeyword(String keyword) {
		if( getCommandString().equalsIgnoreCase(keyword) ) {
			return true;
		}
		return false;
	}

	@Override
	public boolean isDeprecated() {
		return false;
	}

	@Override
	public void reportHelp(PrintStream ps) {
		ps.println("dirdup - Copy Command");
		ps.println();
		ps.println("Command Syntax:");
		ps.println("  dirdup copy <source-dir> <target-dir>");
		ps.println();
		ps.println("  where");
		ps.println("    <source-dir>");
		ps.println("       is the directory where files should be copied from.");
		ps.println();
		ps.println("    <target-dir>");
		ps.println("       is the directory where files should be copied to.");
	}

	@Override
	public void runCommand(GlobalSettings gs, Stack<String> argumentStack) throws Exception {
		// Process arguments
		String srcDirStr = null;
		String targetDirStr = null;
		while( argumentStack.size() > 0 ) {
			String arg = argumentStack.pop();
			
			if( arg.length() > 2 
			 && arg.charAt(0) == '-' 
			 && arg.charAt(1) == '-' ){
				// This is an option
				throw new Exception("Unrecognized option: "+arg);
				
			} else if( null == srcDirStr ){
				srcDirStr = arg;
			
			} else if( null == targetDirStr ){
				targetDirStr = arg;
			
			} else {
				throw new Exception("Unexpected argument: "+arg);
			}
		}
		
		// Check that source and target directories were specified 
		if( null == srcDirStr ){
			throw new Exception("Source directory must be specified");
		}
		if( null == targetDirStr ){
			throw new Exception("Target directory must be specified");
		}
		
		// Check that source directory exists
		File srcDir = new File(srcDirStr);
		gs.getOutStream().println("Source directory: "+srcDir.getAbsolutePath());
		if( !srcDir.exists() ){
			throw new Exception("Source directory does not exist");
		}
		if( !srcDir.isDirectory() ){
			throw new Exception("Source directory is not a directory");
		}
		
		// Test target directory
		File targetDir = new File(targetDirStr);
		if( targetDir.exists() && !targetDir.isDirectory() ){
			throw new Exception("Target directory is not a directory");
		}
		if( false == targetDir.exists() ){
			targetDir.mkdirs();
		}

		// Get list of source files
		FSEntryHead srcHead = new FSEntryHead( new FSEntryFile(srcDir) );
		
		// Make list of hashed named items
		CollectionHead srcHashedHead = new CollectionHead();
		{
			List<DirectoryItem> items = srcHead.getItems();
			for(DirectoryItem item : items){
				HashedItem targetItem = new HashedItem(item);
				srcHashedHead.addItem(targetItem);
				
//				System.out.print(targetItem.getPathAndName());
//				System.out.print(" -> ");
//				System.out.println(item.getPathAndName());
			}
		}
		
		// Get list of target files
		FSEntryHead targetHead = new FSEntryHead( new FSEntryFile(targetDir) );
		
		// Compare source and target
		HeadComparator comparator = new HeadComparator(srcHashedHead, targetHead);
		
		// Delete items
		{
			List<DirectoryItem> itemsToDelete = comparator.getDeletedItems();
			for(DirectoryItem itemToDelete : itemsToDelete){
				File fileToDelete = DirectoryItemUtils.getFile(targetDir, itemToDelete);
				
				System.out.println("Deleting "+fileToDelete.getAbsolutePath());
				fileToDelete.delete();
			}
		}
		
		// Add items
		{
			List<DirectoryItem> itemsToAdd = comparator.getAddedItems();
			for(DirectoryItem itemToAdd : itemsToAdd){
				File fileToAdd = DirectoryItemUtils.getFile(targetDir, itemToAdd);
				
				System.out.println("Copy: "+fileToAdd.getAbsolutePath());
				
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
