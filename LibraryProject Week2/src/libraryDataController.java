package Week2LibraryProjectV3;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class libraryDataController {
	
	private LinkedList<Book> listOfBooks;
	
	public static final String[] nameOfColumns = {
	        
			"book_id",
	        "goodreads_book_id",
	        "best_book_id",
	        "work_id",
	        "books_count",
	        "isbn",
	        "isbn13",
	        "authors",
	        "original_publication_year",
	        "original_title",
	        "title",
	        "language_code",
	        "average_rating",
	        "ratings_count",
	        "work_ratings_count",
	        "work_text_reviews_count",
	        "ratings_1",
	        "ratings_2",
	        "ratings_3",
	        "ratings_4",
	        "ratings_5",
	        "image_url",
	        "small_image_url"};
	
	public libraryDataController() {
		listOfBooks = new LinkedList<>();
	}
		
	public void loadBookData (String nameOfFile) {
		
		String csvSplitRegex = ",(?=([^\"]*\"[^\"]*\")*[^\"]*$)";
		
		try (BufferedReader br = new BufferedReader(new FileReader(nameOfFile))) {
			String line;
			boolean header = true;
			
			while ((line = br.readLine()) != null) {
				if (header) {
					header = false;
					continue;
				}
				
				String[] pieceOfData = line.split(csvSplitRegex, -1);
				String[] preppedData = new String[nameOfColumns.length];
				
				for (int i = 0; i < nameOfColumns.length; i++) {
					if (i < pieceOfData.length) {
						preppedData[i] = pieceOfData[i].replaceAll("^\"|\"$", "").trim();
					}
					
					else {
						preppedData[i] = "";
					}
				}
				
				listOfBooks.add(new Book(preppedData));
				
			}
		}
		
		catch (IOException e) {
			System.err.println("Error reading " + nameOfFile + ": " + e.getMessage());
		}
	}
	
	public List<Book> LinearSearch (String query, String type) {
		
		List<Book> results = new LinkedList<>();
		if (query.isEmpty())
			return results;
		
		for (Book book : listOfBooks) {
			if (type.equals("book_id") && book.getBookId().equalsIgnoreCase(query)) {
				results.add(book);
			}
			
			else if (type.equals("isbn") && book.getIsbn().equalsIgnoreCase(query)) {
				results.add(book);
			}
		}
		
		return results;
		
	}
	
	public void sortBooks (String category, boolean ascending) {
		
		listOfBooks.sort((b1, b2) -> {
			int comparison = 0;
			if (category.equals("Authors")) {
				comparison = b1.getAuthors().compareToIgnoreCase(b2.getAuthors());
			}
			
			else if (category.equals("Original Publication Year")) {
				comparison = Double.compare(b1.getPublicationYear(), b2.getPublicationYear());
			}
			
			return ascending ? comparison : -comparison;
		});
			
  }
	
	public LinkedList<Book> getBooksList() {
        return listOfBooks;
    }
	
}