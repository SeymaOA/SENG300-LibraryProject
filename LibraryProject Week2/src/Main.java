package Week2LibraryProject;

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import static java.lang.System.out;

public class Main{
	public static void main(String[] args) throws FileNotFoundException{
		Library library = new Library();
		library.readCSV("C:/Users/seymapc/eclipse-workspace/Thinkdast-SENG300/src/books.csv");

		library.sortByAuthors(library.books); // should have a button to click for sort in gui
		
		out.println("Sorted by authors: ");
		
		for(int i = 0; i < library.books.size(); i++) {
			out.println(library.books.get(i));
		}
	}
}
