package Week2LibraryProjectV2;

import java.util.ArrayList;
import java.util.List;

public class PerformanceTest {

    // Test ArrayList binary search
    public static long testBinarySearch(
            Library library,
            ArrayList<String> testIds) {

        long startTime = System.nanoTime();

        for (String id : testIds) {
            library.binarySearch(id, "book_id");
        }

        long endTime = System.nanoTime();

        return endTime - startTime;
    }


    // Test LinkedList linear search
    public static long testLinearSearch(
            LibraryManager manager,
            ArrayList<String> testIds) {

        long startTime = System.nanoTime();

        for (String id : testIds) {
            manager.linearSearch(id, "book_id");
        }

        long endTime = System.nanoTime();

        return endTime - startTime;
    }


    public static void main(String[] args) {

        String fileName = "data/books.csv";

        try {

            // -----------------------------
            // Load ArrayList version
            // -----------------------------

            Library arrayLibrary =
                    new Library();

            arrayLibrary.readCSV(fileName);


            // -----------------------------
            // Load LinkedList version
            // -----------------------------

            LibraryManager linkedLibrary =
                    new LibraryManager();

            linkedLibrary.loadData(fileName);


            // -----------------------------
            // Get 500 IDs for testing
            // -----------------------------

            ArrayList<String> testIds =
                    new ArrayList<String>();


            List<Book> books =
                    arrayLibrary.getBooksList();


            int numberOfSearches = 500;


            // If dataset has fewer than 500 books
            if (books.size() < numberOfSearches) {

                numberOfSearches =
                        books.size();
            }


            for (int i = 0;
                 i < numberOfSearches;
                 i++) {

                testIds.add(
                        books.get(i)
                             .getBook_id());
            }


            // -----------------------------
            // Binary Search Test
            // -----------------------------

            long binaryTime =
                    testBinarySearch(
                            arrayLibrary,
                            testIds);


            // -----------------------------
            // Linear Search Test
            // -----------------------------

            long linearTime =
                    testLinearSearch(
                            linkedLibrary,
                            testIds);


            // -----------------------------
            // Print Results
            // -----------------------------

            System.out.println(
                    "PERFORMANCE TEST");

            System.out.println(
                    "Number of searches: "
                    + numberOfSearches);

            System.out.println();


            System.out.println(
                    "ArrayList Binary Search:");

            System.out.println(
                    binaryTime
                    + " nanoseconds");

            System.out.println(
                    binaryTime / 1_000_000.0
                    + " milliseconds");


            System.out.println();


            System.out.println(
                    "LinkedList Linear Search:");

            System.out.println(
                    linearTime
                    + " nanoseconds");

            System.out.println(
                    linearTime / 1_000_000.0
                    + " milliseconds");


            System.out.println();


            // -----------------------------
            // Show which was faster
            // -----------------------------

            if (binaryTime < linearTime) {

                System.out.println(
                        "Binary search was faster.");

            }

            else if (linearTime < binaryTime) {

                System.out.println(
                        "Linear search was faster.");

            }

            else {

                System.out.println(
                        "The times were equal.");
            }

        }

        catch (Exception e) {

            System.out.println(
                    "Error: "
                    + e.getMessage());
        }
    }
}