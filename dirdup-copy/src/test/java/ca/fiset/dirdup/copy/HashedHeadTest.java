package ca.fiset.dirdup.copy;

import java.io.File;
import java.util.List;

import junit.framework.TestCase;

public class HashedHeadTest extends TestCase {
	
	public void testCreation() throws Exception {
		File resourceDir = TestUtils.getResourceDirectory();
		DiskHead head = new DiskHead( new File(resourceDir,"src") );
		HashedHead hashedHead = new HashedHead(head);
		
		List<DirectoryItem> items = hashedHead.getItems();
		for(DirectoryItem item : items){
			System.out.println(item.getPathAndName());
		}
	}

	public void testHashedCopy() throws Exception {
		File resourceDir = TestUtils.getResourceDirectory();
		File targetDir = TestUtils.getTestDirectory("testHashedCopy");
		DiskHead head = new DiskHead( new File(resourceDir,"src") );
		HashedHead hashedHead = new HashedHead(head);
		HeadCopier copier = new HeadCopier();
		copier.copyHead(hashedHead, targetDir);
	}
}
