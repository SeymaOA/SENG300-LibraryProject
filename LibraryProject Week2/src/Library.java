package Week2LibraryProjectV2;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Library {

	private String currentFileName;

	// Main ArrayList
	private final ArrayList<Book> books = new ArrayList<Book>();

	// Separate sorted lists used for binary search
	private final ArrayList<Book> booksById = new ArrayList<Book>();
	private final ArrayList<Book> booksByIsbn = new ArrayList<Book>();


	// Regex for reading CSV fields
	private static final Pattern CSV_FIELD = Pattern.compile(
			"\\G(?:\"((?:[^\"]|\"\")*)\"|([^,\"\r\n]*))(,|$)");


	// =========================================================
	// READ CSV
	// =========================================================

	public void readCSV(String fileName) throws IOException {

		currentFileName = fileName;
		ArrayList<Book> loaded = readRecords(fileName);

		books.clear();
		books.addAll(loaded);

		// Prepare sorted copies for binary searching
		prepareSearchIndexes();
	}

	static ArrayList<Book> readRecords(String fileName)throws IOException {

		ArrayList<Book> loaded = new ArrayList<Book>();
		Scanner scnr = new Scanner(new File(fileName), "UTF-8");

		if (!scnr.hasNextLine()) { // For make sure file is not empty
			scnr.close();
			throw new IOException("The CSV file is empty.");
		}

		String header = readRecord(scnr); // For reading header

		if (header.startsWith("\uFEFF")) { // Remove invisible UTF-8 marker if it exists
			header = header.substring(1);
		}

		if (!Arrays.equals(parseRecord(header),Book.COLUMN_NAMES)) { // Check that the CSV columns match Book.COLUMN_NAMES
			scnr.close();
			throw new IOException("CSV headers do not match the 23 book columns.");}
		
		int recordNumber = 1;
		
		while (scnr.hasNextLine()) { // Read every book record
			String record = readRecord(scnr);

			if (record.isBlank()) {
				continue;
				}
			recordNumber++;

			try {String[] fields = parseRecord(record);
			Book book = new Book(fields);
			loaded.add(book);
			}
			catch (IOException | IllegalArgumentException e) {
				scnr.close();
				throw new IOException("CSV record "+ recordNumber+ ": "+ e.getMessage(), e);
			}
		}
		scnr.close();
		return loaded;
	}

	
	private static String readRecord(Scanner scnr)throws IOException { // Reads one complete CSV record
		StringBuilder record = new StringBuilder();
		boolean insideQuotes = false;

		do { if (!scnr.hasNextLine()) {
			throw new IOException("A quoted CSV cell was never closed.");}
		if (record.length() > 0) {
			record.append('\n');
		}
		String line = scnr.nextLine();
		record.append(line);
		
		for (int i = 0; i < line.length(); i++) { // Check quotation marks
			if (line.charAt(i) == '"') {
				insideQuotes = !insideQuotes;}
		}

		}
		while (insideQuotes);
		return record.toString();
	}


	
	private static String[] parseRecord(String record)throws IOException { // Break one CSV record into 23 fields

		ArrayList<String> cells =new ArrayList<String>();

		Matcher matcher =CSV_FIELD.matcher(record);
		boolean finished = false;
		int end = 0;
		while (matcher.find()) {
			String cell = matcher.group(1);
			// If it was not a quoted field
			if (cell == null) {
				cell = matcher.group(2);
			}

			// If it was quoted
			else {
				cell = cell.replace("\"\"", "\"");
			}
			cells.add(cell.trim());
			end = matcher.end();

			// End of CSV record
			if (matcher.group(3).isEmpty()) {
				finished = true;
				break;
			}
		}


		if (!finished || end != record.length()) {
			throw new IOException("Invalid CSV quoting or separator.");}
		if (cells.size() != Book.COLUMN_NAMES.length) {
			throw new IOException("Expected 23 fields; found "+ cells.size()+ ".");}
		return cells.toArray(new String[0]);
	}

	// =========================================================
	// GET BOOK LIST
	// =========================================================
	public List<Book> getBooksList() {return Collections.unmodifiableList(books);}
	

	//===================================================
	// Automatic BookID generator
	//==============================================================
	public String getNextBookId() {
		int highestId = 0;
		for(Book book: books) {
			try {
				int id = Integer.parseInt(book.getBook_id());
				if(id > highestId) {highestId = id;}
			}
			catch (NumberFormatException e){
			}
			return String.valueOf(highestId + 1);
		}
		return String.valueOf(highestId + 1);
	}

	// =========================================================
	// BINARY SEARCH PREPARATION
	// =========================================================
	public void prepareSearchIndexes() {

		// Copy books into book_id search list
		booksById.clear();
		booksById.addAll(books);


		// Copy books into ISBN search list
		booksByIsbn.clear();
		booksByIsbn.addAll(books);

		// Sort each list for binary search
		booksById.sort(searchComparator("book_id"));

		booksByIsbn.sort(searchComparator("isbn"));
	}



	// =========================================================
	// BINARY SEARCH
	// =========================================================
	public List<Book> binarySearch(String query,String type) {
		
		checkSearchType(type);
		
		ArrayList<Book> results =new ArrayList<Book>();
		if (query == null|| query.trim().isEmpty()) {
			return results;
		}

		query = query.trim();
		ArrayList<Book> sorted;
		if (type.equals("book_id")) {
			sorted = booksById;
		}else {
			sorted = booksByIsbn;
		}

		int low = 0;
		int high = sorted.size() - 1;

		while (low <= high) {
			int mid = (low + high) / 2;
			Book middleBook = sorted.get(mid);
			String middleValue = keyOf(middleBook, type);
			int comparison = middleValue.compareToIgnoreCase(query);

			if (comparison == 0) {
				int start = mid;
				// Move left to first matching value
				while (start > 0 && keyOf(sorted.get(start - 1), type).equalsIgnoreCase(query)) {
					start--;
				}

				// Add all matching values
				for (int i = start;i < sorted.size();i++) {
					if (!keyOf(sorted.get(i),type).equalsIgnoreCase(query)) {
						break;
					}
					results.add(sorted.get(i));
				}
				return results;
			}
			else if (comparison < 0) {
				low = mid + 1;
			}else {
				high = mid - 1;
			}
		}
		return results;
	}
	// Checking allowed search types
	static void checkSearchType(String type) {

		if (!"book_id".equals(type)&& !"isbn".equals(type)) {
			throw new IllegalArgumentException("Search by book_id or isbn.");
		}
	}
	// Getting correct value depending on search type
	static String keyOf(Book book,String type) {
		if ("book_id".equals(type)) {
			return book.getBook_id();
		}else {
			return book.getIsbn();
		}
	}



	// Comparator used to sort lists before binary search
	private static Comparator<Book> searchComparator(final String type) {
		return new Comparator<Book>() {

			@Override
			public int compare(Book a, Book b) {
				return keyOf(a, type).compareToIgnoreCase(keyOf(b, type));
			}
		};
	}



	// =========================================================
	// BOOK_ID BINARY SEARCH
	// =========================================================

	public void sortByBookId() {

		books.sort(new Comparator<Book>() {

			@Override
			public int compare(
					Book a,
					Book b) {

				int idA =Integer.parseInt(a.getBook_id());

				int idB =Integer.parseInt(b.getBook_id());
				return Integer.compare(idA,idB);
			}
		});
	}


	public Book binarySearchByBookId(String bookId) {

		int target = Integer.parseInt(bookId);

		int low = 0;
		int high = books.size() - 1;

		while (low <= high) {
			int middle = (low + high) / 2;
			Book middleBook = books.get(middle);
			int middleId = Integer.parseInt(middleBook.getBook_id());

			if (middleId == target) {
				return middleBook;
			}
			else if (target < middleId) {
				high = middle - 1;
			}
			else {
				low = middle + 1;
			}
		}
		return null;
	}

	// =========================================================
	// SORTING
	// =========================================================

	public void sortBooks(String criteria, boolean ascending) {

		sortList(books, criteria,ascending);
	}

	// Optional shortcut method
	public void sortByAuthors(boolean ascending) {
		sortBooks("Authors",ascending);
	}


	// Shared sorting method
	// LibraryManager also uses this
	static void sortList(List<Book> list, final String criteria, final boolean ascending) {
		if (!"Authors".equals(criteria)&& !"Original Publication Year".equals(criteria)) {
			throw new IllegalArgumentException(	"Unknown sort criterion: "+ criteria);
		}

		list.sort(new Comparator<Book>() {

			@Override
			public int compare(Book a,	Book b) {
				int comparison;

	// ---------------------------------------------------------
	// SORT BY AUTHORS
	// ---------------------------------------------------------
				if (criteria.equals("Authors")) {	
					String first =	a.getAuthors();

					String second =b.getAuthors();


					// Put empty values at end
					if (first.isEmpty()) {
						if (second.isEmpty()) {
							return 0;
						}
						return 1;
					}
					if (second.isEmpty()) {
						return -1;
					}
					comparison =first.compareToIgnoreCase(second);
				}


		// -----------------------------------------------------
		// SORT BY PUBLICATION YEAR
		// -----------------------------------------------------
					else {

						Double first =a.getPublicationYear();
						Double second =b.getPublicationYear();

					// Missing years stay at end
					if (first == null) {
						if (second == null) {
							return 0;
						}
						return 1;
					}
					if (second == null) {
						return -1;
					}
					comparison =Double.compare(	first,	second);
				}
				// Ascending or descending
				if (ascending) {
					return comparison;
				}

				else {
					return -comparison;
				}
			}
		});
	}



	// =========================================================
	// ADD BOOK
	// =========================================================
	public void addBook(Book book) {

		books.add(book);
		// Rebuild binary-search lists
		prepareSearchIndexes();
	}

	//============================================================
	// Get top 10 by rating
	//=============================================================
	public List<Book> getTop10ByRating() {

		// Make a copy so the original ArrayList order does not change
		ArrayList<Book> sortedBooks = new ArrayList<Book>(books);
		sortedBooks.sort(new Comparator<Book>() {

		@Override
			public int compare(Book a, Book b) {

				double ratingA = 0;
				double ratingB = 0;

				try {ratingA =Double.parseDouble(a.getAverage_rating());
				}
				catch (Exception e) {
					ratingA = 0;
				}

				try {
					ratingB =Double.parseDouble(b.getAverage_rating());
				}
				catch (Exception e) {
					ratingB = 0;
				}

				// Descending: highest rating first
				return Double.compare(ratingB,ratingA);
			}
		});


		ArrayList<Book> topTen = new ArrayList<Book>();

		int limit = Math.min(10, sortedBooks.size());

		for (int i = 0; i < limit; i++) {

			topTen.add(sortedBooks.get(i));
		}
		return topTen;
	}

	// =========================================================
	// DELETE BOOK
	// =========================================================
	public boolean deleteBook(String bookId) {
		for (int i = 0;i < books.size();i++) {
			if (books.get(i).getBook_id().equals(bookId)) {
				books.remove(i);
				// Rebuild binary-search lists
				prepareSearchIndexes();
				return true;
			}
		}
		return false;
	}
}
