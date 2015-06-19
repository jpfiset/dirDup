package ca.fiset.dirdup.copy;

import java.io.File;
import java.util.List;

import ca.carleton.gcrc.couch.fsentry.FSEntryFile;
import junit.framework.TestCase;

public class DiskHeadTest extends TestCase {

	public void testCreation() throws Exception {
		File resourceDir = TestUtils.getResourceDirectory();
		DiskHead head = new DiskHead( new FSEntryFile( new File(resourceDir,"src") ) );
		List<DirectoryItem> items = head.getItems();
		for(DirectoryItem item : items){
			System.out.println(item.getPathAndName());
		}
	}

	public void testDiskCopy() throws Exception {
		File resourceDir = TestUtils.getResourceDirectory();
		File targetDir = TestUtils.getTestDirectory("testDiskCopy");
		DiskHead head = new DiskHead( new FSEntryFile( new File(resourceDir,"src") ) );
		HeadCopier copier = new HeadCopier();
		copier.copyHead(head, targetDir);
	}
}
