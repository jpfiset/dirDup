package ca.fiset.dirdup.command;

import java.io.File;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Stack;

import ca.fiset.dirdup.copy.DiskHead;
import ca.fiset.dirdup.copy.HashedHead;
import ca.fiset.dirdup.copy.HeadCopier;

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
		DiskHead srcHead = new DiskHead( srcDir );
		
		// Make list of hashed named items
		HashedHead srcHashedHead = new HashedHead(srcHead);

		// Copy
		HeadCopier copier = new HeadCopier();
		copier.setPrintWriter( new PrintWriter(System.out) );
		copier.copyHead(srcHashedHead, targetDir);
	}
}
