package Week2LibraryProjectV3;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/** Stores  books in an ArrayList and searches with binary search. */
public class LibrarySeyma {
    private ArrayList<BookSeyma> books = new ArrayList<BookSeyma>();
    private ArrayList<BookSeyma> booksById = new ArrayList<BookSeyma>();
    private ArrayList<BookSeyma> booksByIsbn = new ArrayList<BookSeyma>();

    public void readCSV(String fileName) throws IOException {
        // Finish reading before replacing the current books.
        ArrayList<BookSeyma> loaded = readRecords(fileName);
        books = loaded;
        prepareSearchIndexes();
    }

    static ArrayList<BookSeyma> readRecords(String fileName) throws IOException {
        ArrayList<BookSeyma> loaded = new ArrayList<BookSeyma>();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(fileName), "UTF-8"))) {
            String header = readRecord(reader);
            if (header == null) {
                throw new IOException("The CSV file is empty.");
            }
            if (header.startsWith("\uFEFF")) {
                header = header.substring(1);
            }
            String[] columns = parseRecord(header);
            for (int i = 0; i < columns.length; i++) {
                if (!columns[i].equals(BookSeyma.COLUMN_NAMES[i])) {
                    throw new IOException("CSV headers do not match the 23 book columns.");
                }
            }
            String record;
            int recordNumber = 1;
            while ((record = readRecord(reader)) != null) {
                if (record.trim().isEmpty()) {
                    continue;
                }
                recordNumber++;
                try {
                    loaded.add(new BookSeyma(parseRecord(record)));
                } catch (IOException e) {
                    throw new IOException("CSV record " + recordNumber + ": " + e.getMessage(), e);
                }
            }
        }
        return loaded;
    }

    // A quoted CSV field can contain a line break, so a record may span lines.
    private static String readRecord(BufferedReader reader) throws IOException {
        String line = reader.readLine();
        if (line == null) {
            return null;
        }
        StringBuilder record = new StringBuilder();
        boolean insideQuotes = false;
        while (true) {
            record.append(line);
            for (int i = 0; i < line.length(); i++) {
                if (line.charAt(i) == '"') {
                    insideQuotes = !insideQuotes;
                }
            }
            if (!insideQuotes) {
                return record.toString();
            }
            line = reader.readLine();
            if (line == null) {
                throw new IOException("A quoted CSV field was never closed.");
            }
            record.append('\n');
        }
    }

    // Read characters instead of using a complicated regular expression.
    private static String[] parseRecord(String record) throws IOException {
        ArrayList<String> fields = new ArrayList<String>();
        StringBuilder value = new StringBuilder();
        boolean insideQuotes = false;
        boolean closedQuotes = false;
        for (int i = 0; i < record.length(); i++) {
            char letter = record.charAt(i);
            if (insideQuotes) {
                if (letter == '"') {
                    if (i + 1 < record.length() && record.charAt(i + 1) == '"') {
                        value.append('"');
                        i++;
                    } else {
                        insideQuotes = false;
                        closedQuotes = true;
                    }
                } else {
                    value.append(letter);
                }
            } else if (letter == ',') {
                fields.add(value.toString().trim());
                value.setLength(0);
                closedQuotes = false;
            } else if (letter == '"' && value.length() == 0 && !closedQuotes) {
                insideQuotes = true;
            } else if (letter == '"' || closedQuotes) {
                throw new IOException("Invalid CSV quotation marks.");
            } else {
                value.append(letter);
            }
        }
        if (insideQuotes) {
            throw new IOException("A quoted CSV field was never closed.");
        }
        fields.add(value.toString().trim());
        if (fields.size() != BookSeyma.COLUMN_NAMES.length) {
            throw new IOException("Expected 23 fields; found " + fields.size() + ".");
        }
        return fields.toArray(new String[fields.size()]);
    }

    public List<BookSeyma> getBooksList() {
        // Call addBook/deleteBook to change the library, not this returned list.
        return new ArrayList<BookSeyma>(books);
    }

    public String getNextBookId() {
        int highestId = 0;
        for (BookSeyma book : books) {
            try {
                int id = Integer.parseInt(book.getBook_id());
                if (id > highestId) {
                    highestId = id;
                }
            } catch (NumberFormatException e) {
                // Non-numeric IDs do not affect the next numeric ID.
            }
        }
        if (highestId == Integer.MAX_VALUE) {
            throw new IllegalArgumentException("No more numeric book IDs are available.");
        }
        return String.valueOf(highestId + 1);
    }

    public void prepareSearchIndexes() {
        // Display sorting must not disturb the order needed for binary search.
        booksById = new ArrayList<BookSeyma>(books);
        booksByIsbn = new ArrayList<BookSeyma>(books);
        sortList(booksById, "book_id", true);
        sortList(booksByIsbn, "isbn", true);
    }

    public List<BookSeyma> binarySearch(String query, String type) {
        if (!"book_id".equals(type) && !"isbn".equals(type)) {
            throw new IllegalArgumentException("Search by book_id or isbn.");
        }
        ArrayList<BookSeyma> results = new ArrayList<BookSeyma>();
        if (query == null || query.trim().isEmpty()) {
            return results;
        }
        query = query.trim();
        ArrayList<BookSeyma> sorted = booksById;
        if (type.equals("isbn")) {
            sorted = booksByIsbn;
        }
        int low = 0;
        int high = sorted.size() - 1;
        while (low <= high) {
            int middle = low + (high - low) / 2;
            int comparison = searchValue(sorted.get(middle), type).compareToIgnoreCase(query);
            if (comparison < 0) {
                low = middle + 1;
            } else if (comparison > 0) {
                high = middle - 1;
            } else {
                // ISBNs can repeat. Find the first match, then collect all matches.
                int first = middle;
                while (first > 0 && searchValue(sorted.get(first - 1), type).equalsIgnoreCase(query)) {
                    first--;
                }
                int i = first;
                while (i < sorted.size() && searchValue(sorted.get(i), type).equalsIgnoreCase(query)) {
                    results.add(sorted.get(i));
                    i++;
                }
                return results;
            }
        }
        return results;
    }

    private static String searchValue(BookSeyma book, String type) {
        if (type.equals("book_id")) {
            return book.getBook_id();
        }
        return book.getIsbn();
    }

    public BookSeyma binarySearchByBookId(String bookId) {
        // Use the ID index even after the display was sorted by author.
        List<BookSeyma> results = binarySearch(bookId, "book_id");
        if (results.isEmpty()) {
            return null;
        }
        return results.get(0);
    }

    public void sortByBookId() {
        sortList(books, "Numeric Book ID", true);
    }

    public void sortByAuthors(boolean ascending) {
        sortBooks("Authors", ascending);
    }

    public void sortBooks(String category, boolean ascending) {
        if (!"Authors".equals(category) && !"Original Publication Year".equals(category)) {
            throw new IllegalArgumentException("Unknown sort category: " + category);
        }
        sortList(books, category, ascending);
    }

    // Java does the sorting. A Comparator explains how to compare two books.
    private static void sortList(List<BookSeyma> list, final String category, final boolean ascending) {
        Collections.sort(list, new Comparator<BookSeyma>() {
            @Override
            public int compare(BookSeyma first, BookSeyma second) {
                int comparison;
                if (category.equals("book_id") || category.equals("isbn")) {
                    comparison = searchValue(first, category).compareToIgnoreCase(searchValue(second, category));
                } else if (category.equals("Authors")) {
                    String a = first.getAuthors();
                    String b = second.getAuthors();
                    if (a.isEmpty() && b.isEmpty()) {
                        return 0;
                    }
                    if (a.isEmpty()) {
                        return 1;
                    }
                    if (b.isEmpty()) {
                        return -1;
                    }
                    comparison = a.compareToIgnoreCase(b);
                } else if (category.equals("Original Publication Year")) {
                    Double a = first.getPublicationYear();
                    Double b = second.getPublicationYear();
                    // Missing years stay last in both directions.
                    if (a == null && b == null) {
                        return 0;
                    }
                    if (a == null) {
                        return 1;
                    }
                    if (b == null) {
                        return -1;
                    }
                    comparison = Double.compare(a, b);
                } else if (category.equals("Numeric Book ID")) {
                    comparison = Double.compare(numberOrZero(first.getBook_id()), numberOrZero(second.getBook_id()));
                } else {
                    comparison = Double.compare(numberOrZero(first.getAverage_rating()),
                            numberOrZero(second.getAverage_rating()));
                }
                if (ascending) {
                    return comparison;
                }
                return -comparison;
            }
        });
    }

    static double numberOrZero(String text) {
        if (text == null) {
            return 0;
        }
        try {
            double number = Double.parseDouble(text);
            if (Double.isNaN(number) || Double.isInfinite(number)) {
                return 0;
            }
            return number;
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public List<BookSeyma> getTop10ByRating() {
        ArrayList<BookSeyma> sorted = new ArrayList<BookSeyma>(books);
        sortList(sorted, "Average Rating", false);
        ArrayList<BookSeyma> topTen = new ArrayList<BookSeyma>();
        for (int i = 0; i < sorted.size() && i < 10; i++) {
            topTen.add(sorted.get(i));
        }
        return topTen;
    }

    public void addBook(BookSeyma book) {
        if (book == null || book.getBook_id().isEmpty()) {
            throw new IllegalArgumentException("A book must have a book ID.");
        }
        if (!binarySearch(book.getBook_id(), "book_id").isEmpty()) {
            throw new IllegalArgumentException("That book ID already exists.");
        }
        books.add(book);
        prepareSearchIndexes();
    }

    public boolean deleteBook(String bookId) {
        if (bookId == null) {
            return false;
        }
        for (int i = 0; i < books.size(); i++) {
            if (books.get(i).getBook_id().equalsIgnoreCase(bookId.trim())) {
                books.remove(i);
                prepareSearchIndexes();
                return true;
            }
        }
        return false;
    }
}
