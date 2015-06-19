package ca.fiset.dirdup.command;

import java.io.PrintStream;
import java.util.Stack;

public interface Command {
	
	String getCommandString();
	
	boolean matchesKeyword(String keyword);
	
	boolean isDeprecated();
	
	void reportHelp(PrintStream ps);
	
	void runCommand(
		GlobalSettings gs
		,Stack<String> argumentStack
		) throws Exception;

}
