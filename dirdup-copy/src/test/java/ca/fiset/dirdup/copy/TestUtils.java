package ca.fiset.dirdup.copy;

import java.io.File;
import java.net.URL;
import java.net.URLDecoder;

public class TestUtils {
	static public File getResourceDirectory() throws Exception {
		URL dummyUrl = TestUtils.class.getClassLoader().getResource("testDummy.txt");
		String fileName = dummyUrl.getFile();
		String decodedFilename = URLDecoder.decode(fileName,"UTF-8");
		File dummyFile = new File(decodedFilename);
		File parent = dummyFile.getParentFile();
		
		return parent;
	}
	
	static private File g_testDir = null;
	synchronized static public File getTestDirectory() throws Exception {
		if( null == g_testDir ){
			File parent = getResourceDirectory();
			
			for(int i=0; i<1000; ++i){
				String dirName = String.format("test%04d", i);
				File testDir = new File(parent,dirName);
				if( false == testDir.exists() ){
					testDir.mkdirs();
					g_testDir = testDir;
					break;
				}
			}
			
			if( null == g_testDir ){
				throw new Exception("Unable to create test directory. Clean up your project.");
			}
		}
		
		return g_testDir;
	}

	synchronized static public File getTestDirectory(String dirName) throws Exception {
		File parentTestDir = getTestDirectory();
		File testDir = new File(parentTestDir, dirName);
		testDir.mkdirs();
		return testDir;
	}
}
