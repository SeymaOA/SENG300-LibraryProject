package Week2LibraryProjectV3;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** A classroom timing comparison using the same searches for both libraries. */
public class PerformanceTest {
    // Save the number of matches so timed searches do observable work.
    private static long lastMatchCount;

    public static long testBinarySearch(LibrarySeyma library, ArrayList<String> queries) {
        return testBinarySearch(library, queries, "book_id");
    }

    public static long testBinarySearch(LibrarySeyma library, ArrayList<String> queries, String type) {
        long matches = 0;
        long start = System.nanoTime();
        for (String query : queries) {
            matches += library.binarySearch(query, type).size();
        }
        long elapsed = System.nanoTime() - start;
        lastMatchCount = matches;
        return elapsed;
    }

    public static long testLinearSearch(libraryDataController library, ArrayList<String> queries) {
        return testLinearSearch(library, queries, "book_id");
    }

    public static long testLinearSearch(libraryDataController library, ArrayList<String> queries, String type) {
        long matches = 0;
        long start = System.nanoTime();
        for (String query : queries) {
            matches += library.LinearSearch(query, type).size();
        }
        long elapsed = System.nanoTime() - start;
        lastMatchCount = matches;
        return elapsed;
    }

    private static ArrayList<String> makeQueries(List<BookSeyma> books, String type) {
        ArrayList<String> queries = new ArrayList<String>();
        int sampleSize = Math.min(500, books.size());
        for (int i = 0; i < sampleSize; i++) {
            // Sample throughout the file, including its first and last books.
            int position = 0;
            if (sampleSize > 1) {
                position = (int) ((long) i * (books.size() - 1) / (sampleSize - 1));
            }
            BookSeyma book = books.get(position);
            String query = book.getBook_id();
            if (type.equals("isbn")) {
                query = book.getIsbn();
            }
            if (!query.isEmpty()) {
                queries.add(query);
            }
        }
        return queries;
    }

    private static long checkResults(LibrarySeyma arrayLibrary, libraryDataController linkedLibrary,
            ArrayList<String> queries, String type) {
        long matches = 0;
        for (String query : queries) {
            ArrayList<String> arrayIds = new ArrayList<String>();
            for (BookSeyma book : arrayLibrary.binarySearch(query, type)) {
                arrayIds.add(book.getBook_id());
            }
            ArrayList<String> linkedIds = new ArrayList<String>();
            for (Book book : linkedLibrary.LinearSearch(query, type)) {
                linkedIds.add(book.getBookId());
            }
            Collections.sort(arrayIds);
            Collections.sort(linkedIds);
            if (!arrayIds.equals(linkedIds)) {
                throw new IllegalArgumentException("The libraries returned different results for " + type + " " + query);
            }
            matches += arrayIds.size();
        }
        return matches;
    }

    public static void main(String[] args) {
        String fileName = "data/books_1.csv";
        String type = "book_id";
        if (args.length > 0) {
            fileName = args[0];
        }
        if (args.length > 1) {
            type = args[1];
        }
        try {
            if (!type.equals("book_id") && !type.equals("isbn")) {
                throw new IllegalArgumentException("The search type must be book_id or isbn.");
            }
            LibrarySeyma arrayLibrary = new LibrarySeyma();
            long start = System.nanoTime();
            arrayLibrary.readCSV(fileName);
            long arrayLoadTime = System.nanoTime() - start;

            libraryDataController linkedLibrary = new libraryDataController();
            start = System.nanoTime();
            linkedLibrary.loadBookData(fileName);
            long linkedLoadTime = System.nanoTime() - start;

            List<BookSeyma> books = arrayLibrary.getBooksList();
            if (books.isEmpty()) {
                throw new IllegalArgumentException("There are no books to test.");
            }
            if (books.size() != linkedLibrary.getBooksList().size()) {
                throw new IllegalArgumentException("The two libraries did not load the same number of books.");
            }
            ArrayList<String> queries = makeQueries(books, type);
            if (queries.isEmpty()) {
                throw new IllegalArgumentException("There are no non-empty values for " + type + ".");
            }
            int hitQueries = queries.size();
            String missing = "__missing_value__";
            while (!arrayLibrary.binarySearch(missing, type).isEmpty()
                    || !linkedLibrary.LinearSearch(missing, type).isEmpty()) {
                missing += "_";
            }
            int missQueries = Math.max(1, hitQueries / 10);
            for (int i = 0; i < missQueries; i++) {
                queries.add(missing);
            }
            long expectedMatches = checkResults(arrayLibrary, linkedLibrary, queries, type);

            // Warm up both search methods before recording times.
            for (int i = 0; i < 3; i++) {
                testBinarySearch(arrayLibrary, queries, type);
                testLinearSearch(linkedLibrary, queries, type);
            }
            long binaryTotal = 0;
            long linearTotal = 0;
            int rounds = 6;
            for (int i = 0; i < rounds; i++) {
                // Alternate which implementation runs first.
                if (i % 2 == 0) {
                    binaryTotal += testBinarySearch(arrayLibrary, queries, type);
                    linearTotal += testLinearSearch(linkedLibrary, queries, type);
                } else {
                    linearTotal += testLinearSearch(linkedLibrary, queries, type);
                    binaryTotal += testBinarySearch(arrayLibrary, queries, type);
                }
                if (lastMatchCount != expectedMatches) {
                    throw new IllegalStateException("Search results changed during the timing test.");
                }
            }
            System.out.println("PERFORMANCE TEST - " + type);
            System.out.println("Books loaded by each library: " + books.size());
            System.out.println("Queries per round: " + hitQueries + " present, " + missQueries + " missing");
            System.out.println("Matching book IDs verified for every query. Matches per round: " + expectedMatches);
            System.out.printf("ArrayList loading + search-index preparation: %.3f ms%n", arrayLoadTime / 1_000_000.0);
            System.out.printf("LinkedList loading: %.3f ms%n", linkedLoadTime / 1_000_000.0);
            System.out.printf("Average binary search batch (%d rounds): %.3f ms%n", rounds, binaryTotal / (rounds * 1_000_000.0));
            System.out.printf("Average linear search batch (%d rounds): %.3f ms%n", rounds, linearTotal / (rounds * 1_000_000.0));
            System.out.println("Search times exclude loading and sorting. Results vary by data and computer.");
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("Cannot run performance test: " + e.getMessage());
        }
    }
}
