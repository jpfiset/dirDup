package ca.fiset.dirdup.copy;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ca.carleton.gcrc.couch.fsentry.FSEntry;

public class HashedItem implements DirectoryItem {
	
	final static private Pattern patternNameExt = Pattern.compile("^(.*)\\.([^.]*)$");

	private DirectoryItem srcItem;
	private List<String> path;
	private String name;
	
	public HashedItem(DirectoryItem srcItem) throws Exception {
		this.srcItem = srcItem;
		
		// Compute path
		{
			this.path = new ArrayList<String>(1);
			
			if( this.srcItem.getPath().size() > 0 ){
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				OutputStreamWriter osw = new OutputStreamWriter(baos, "UTF-8");
				
				boolean isFirst = true;
				for(String fragPath : this.srcItem.getPath()){
					if( isFirst ){
						isFirst = false;
					} else {
						osw.flush();
						baos.write( (byte)0x00 );
					}
					
					osw.write(fragPath);
				}
				osw.flush();
				
				MessageDigest md = MessageDigest.getInstance("SHA-1");
				byte[] digest = md.digest( baos.toByteArray() );
				
				StringWriter sw = new StringWriter();
				for(int i=0; i<digest.length && i<8; ++i){
					int v = (digest[i] & 0xff);
					String hex = String.format("%02x", v);
					sw.write(hex);
				}
				sw.flush();
				
				this.path.add( sw.toString() );
			}
		}
		
		// Compute name
		{
			String ext = null;
			String name = this.srcItem.getName();
			Matcher matcherNameExt = patternNameExt.matcher(name);
			if( matcherNameExt.matches() ){
				name = matcherNameExt.group(1);
				ext = matcherNameExt.group(2);
			}
			
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			OutputStreamWriter osw = new OutputStreamWriter(baos, "UTF-8");
			osw.write(name);
			osw.flush();
			
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			byte[] digest = md.digest( baos.toByteArray() );
			
			StringWriter sw = new StringWriter();
			for(int i=0; i<digest.length && i<8; ++i){
				int v = (digest[i] & 0xff);
				String hex = String.format("%02x", v);
				sw.write(hex);
			}
			if( null != ext ){
				sw.write(".");
				sw.write(ext);
			}
			sw.flush();
			
			this.name = sw.toString();
		}
	}

	@Override
	public List<String> getPath(){
		return path;
	}
	
	@Override
	public String getName(){
		return name;
	}

	@Override
	public FSEntry getEntry(){
		return srcItem.getEntry();
	}
	
	public DirectoryItem getSourceItem(){
		return srcItem;
	}
	
	@Override
	public String getPathAndName(){
		StringWriter sw = new StringWriter();
		
		for(String fragPath : getPath()){
			sw.write(fragPath);
			sw.write("/");
		}
		
		sw.write(getName());
		
		sw.flush();
		
		return sw.toString();
	}
}
