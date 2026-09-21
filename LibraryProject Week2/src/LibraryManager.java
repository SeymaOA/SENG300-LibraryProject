package Week2LibraryProjectV2;


import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class LibraryManager {
	
	
    private final LinkedList<Book> booksList =
            new LinkedList<Book>();

    public static final String[] COLUMN_NAMES = Book.COLUMN_NAMES;

    public LibraryManager() {
    }

    // Create a separate LinkedList using the already loaded Book objects.
    public LibraryManager(List<Book> source) {
        booksList.addAll(source);
    }

    public void loadData(String fileName) throws IOException {
        List<Book> loaded = Library.readRecords(fileName);

        booksList.clear();
        booksList.addAll(loaded);
    }

    public List<Book> getBooksList() {
        return Collections.unmodifiableList(booksList);
    }

    public List<Book> linearSearch(String query, String type) {
        Library.checkSearchType(type);

        LinkedList<Book> results = new LinkedList<Book>();

        if (query == null || query.trim().isEmpty()) {
            return results;
        }

        query = query.trim();

        for (Book book : booksList) {
            String value = Library.keyOf(book, type);

            if (value.equalsIgnoreCase(query)) {
                results.add(book);
            }
        }

        return results;
    }

    public void sortBooks(String criteria, boolean ascending) {
        Library.sortList(booksList, criteria, ascending);
    }
}