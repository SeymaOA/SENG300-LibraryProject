package Week2LibraryProjectV3;

import java.io.IOException;
import java.util.List;


public class MainSeyma {
    public static void main(String[] args) {
        String fileName = "data/books_1.csv";
        if (args.length > 0) {
            fileName = args[0];
        }

        LibrarySeyma library = new LibrarySeyma();
        try {
            library.readCSV(fileName);
            System.out.println("Books loaded: " + library.getBooksList().size());

            library.sortByAuthors(true);
            System.out.println("\nFirst 10 books sorted by author:");
            printBooks(library.getBooksList(), 10);

            System.out.println("\nBinary search for book ID 1:");
            printBooks(library.binarySearch("1", "book_id"), 10);

            System.out.println("\nTop 10 by average rating:");
            printBooks(library.getTop10ByRating(), 10);
            System.out.println("\nNext available numeric ID: " + library.getNextBookId());
            System.out.println("Run GUI_V3 to use the shared window.");
        } catch (IOException e) {
            System.err.println("Could not load " + fileName + ": " + e.getMessage());
        }
    }

    private static void printBooks(List<BookSeyma> books, int limit) {
        if (books.isEmpty()) {
            System.out.println("No books found.");
            return;
        }
        for (int i = 0; i < books.size() && i < limit; i++) {
            BookSeyma book = books.get(i);
            System.out.println(book);
        }
    }
}
