package Week2LibraryProject;

import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import static java.lang.System.out;

public class Library {
	ArrayList<Book> books = new ArrayList<Book>();
	
	
	String fileLink = "C:/Users/seymapc/eclipse-workspace/Thinkdast-SENG300/src/books.csv";
	Pattern pattern = Pattern.compile("(?:^|,)");
	
	
	public void readCSV(String fileLink) throws FileNotFoundException {

		Scanner scnr = new Scanner(new File(fileLink));
		
	
		
		while(scnr.hasNextLine()) {
			String line = scnr.nextLine();
			String[] fields = line.split(",",-1);
			Matcher matcher = pattern.matcher(line);
			out.println(line);
			
			
			
			if(fields.length !=23) {
				out.println();
				out.println("Fields: " + fields.length);
				out.println(matcher.find());
			}

		}
	

}
	
	

	
	
	
	
	
	
}
