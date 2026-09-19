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
	Pattern pattern = Pattern.compile("(?:^|,)(\\\"[^\\\"]*\\\"|[^,]*)");

	
	
	public void readCSV(String fileLink) throws FileNotFoundException {

		Scanner scnr = new Scanner(new File(fileLink));
		if(scnr.hasNextLine()) {
			scnr.nextLine();
		}

		while(scnr.hasNextLine()) {
			String line = scnr.nextLine();
			//String[] fields = line.split(",",-1);
			Matcher matcher = pattern.matcher(line);
			//out.println(line);
			
			String[] fields = new String[23];
			int index = 0;
	
			while(matcher.find()) {
				//out.println(matcher.group(1));   //To read every  box
				fields[index] = matcher.group(1);	
				index++;
			}
			
			
			Book book = new Book(fields);
			books.add(book);
		}
		
		
		
		
}
	
	

	
	
	
	
	
	
}
