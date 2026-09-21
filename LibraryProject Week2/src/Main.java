package Week2LibraryProjectV2;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        String fileName =
                args.length > 0 ? args[0] : "data/books.csv";

        Library library = new Library();

        try {

            library.readCSV(fileName);

            System.out.println(
                    "Books loaded: "
                    + library.getBooksList().size());


            // Sort authors ascending
            library.sortByAuthors(true);


            System.out.println(
                    "\nFirst 10 books sorted by authors:");

            List<Book> books =
                    library.getBooksList();


            for (int i = 0;
                 i < Math.min(10, books.size());
                 i++) {

                System.out.println(
                        books.get(i));
            }


            System.out.println(
                    "\nBinary search for book_id 1:");


            List<Book> searchResult =
                    library.binarySearch(
                            "1",
                            "book_id");


            if (searchResult.isEmpty()) {

                System.out.println(
                        "Book not found.");

            } else {

                for (Book book : searchResult) {

                    System.out.println(book);
                }
            }

        }

        catch (IOException e) {

            System.err.println(
                    "Could not load: "
                    + new File(fileName)
                    .getAbsolutePath());

            System.err.println(
                    e.getMessage());
        }

        catch (IllegalArgumentException e) {

            System.err.println(
                    "Error: "
                    + e.getMessage());
        }
    }
}
