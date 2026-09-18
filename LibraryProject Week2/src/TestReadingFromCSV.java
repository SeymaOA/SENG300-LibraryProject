package Week2LibraryProject;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.classfile.ClassReader;
import java.util.ArrayList;
import java.util.Scanner;

// There are total 23 coloumns

public class TestReadingFromCSV {
	public static void main(String[] args) throws FileNotFoundException {
		String filePath = "C:/Users/seymapc/Documents/Fall2026/SENG300/week2/books.csv";
	
		Scanner scnr = new Scanner(new File(filePath));
		scnr.useDelimiter(",");
		
		int count = 0;
		while(scnr.hasNext()) {
			System.out.println(scnr.next());
			count++;
		}
		
		System.out.println(count);
		
		
		
		
	}
}
