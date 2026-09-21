package Week2LibraryProjectV2;


public class Book {

 public static final String[] COLUMN_NAMES = {
     "book_id", "goodreads_book_id", "best_book_id", "work_id",
     "books_count", "isbn", "isbn13", "authors",
     "original_publication_year", "original_title", "title",
     "language_code", "average_rating", "ratings_count",
     "work_ratings_count", "work_text_reviews_count",
     "ratings_1", "ratings_2", "ratings_3", "ratings_4",
     "ratings_5", "image_url", "small_image_url"
 };

 private String book_id;
 private String goodreads_book_id;
 private String best_book_id;
 private String work_id;
 private String books_count;
 private String isbn;
 private String isbn13;
 private String authors;
 private String original_publication_year;
 private String original_title;
 private String title;
 private String language_code;
 private String average_rating;
 private String ratings_count;
 private String work_ratings_count;
 private String work_text_reviews_count;
 private String ratings_1;
 private String ratings_2;
 private String ratings_3;
 private String ratings_4;
 private String ratings_5;
 private String image_url;
 private String small_image_url;

 public Book(String[] fields) {
     if (fields == null || fields.length != COLUMN_NAMES.length) {
         throw new IllegalArgumentException(
                 "A book needs exactly 23 fields.");
     }

     book_id = clean(fields[0]);
     goodreads_book_id = clean(fields[1]);
     best_book_id = clean(fields[2]);
     work_id = clean(fields[3]);
     books_count = clean(fields[4]);
     isbn = clean(fields[5]);
     isbn13 = clean(fields[6]);
     authors = clean(fields[7]);
     original_publication_year = clean(fields[8]);
     original_title = clean(fields[9]);
     title = clean(fields[10]);
     language_code = clean(fields[11]);
     average_rating = clean(fields[12]);
     ratings_count = clean(fields[13]);
     work_ratings_count = clean(fields[14]);
     work_text_reviews_count = clean(fields[15]);
     ratings_1 = clean(fields[16]);
     ratings_2 = clean(fields[17]);
     ratings_3 = clean(fields[18]);
     ratings_4 = clean(fields[19]);
     ratings_5 = clean(fields[20]);
     image_url = clean(fields[21]);
     small_image_url = clean(fields[22]);
 }

 // Keep missing cells as empty strings.
 private static String clean(String value) {
     return value == null ? "" : value.trim();
 }

 public String getBook_id() { return book_id; }
 public void setBook_id(String value) {
     book_id = clean(value);
 }

 public String getGoodreads_book_id() { return goodreads_book_id; }
 public void setGoodreads_book_id(String value) {
     goodreads_book_id = clean(value);
 }

 public String getBest_book_id() { return best_book_id; }
 public void setBest_book_id(String value) {
     best_book_id = clean(value);
 }

 public String getWork_id() { return work_id; }
 public void setWork_id(String value) {
     work_id = clean(value);
 }

 public String getBooks_count() { return books_count; }
 public void setBooks_count(String value) {
     books_count = clean(value);
 }

 public String getIsbn() { return isbn; }
 public void setIsbn(String value) {
     isbn = clean(value);
 }

 public String getIsbn13() { return isbn13; }
 public void setIsbn13(String value) {
     isbn13 = clean(value);
 }

 public String getAuthors() { return authors; }
 public void setAuthors(String value) {
     authors = clean(value);
 }

 public String getOriginal_publication_year() {
     return original_publication_year;
 }

 public void setOriginal_publication_year(String value) {
     original_publication_year = clean(value);
 }

 public String getOriginal_title() { return original_title; }
 public void setOriginal_title(String value) {
     original_title = clean(value);
 }

 public String getTitle() { return title; }
 public void setTitle(String value) {
     title = clean(value);
 }

 public String getLanguage_code() { return language_code; }
 public void setLanguage_code(String value) {
     language_code = clean(value);
 }

 public String getAverage_rating() { return average_rating; }
 public void setAverage_rating(String value) {
     average_rating = clean(value);
 }

 public String getRatings_count() { return ratings_count; }
 public void setRatings_count(String value) {
     ratings_count = clean(value);
 }

 public String getWork_ratings_count() { return work_ratings_count; }
 public void setWork_ratings_count(String value) {
     work_ratings_count = clean(value);
 }

 public String getWork_text_reviews_count() {
     return work_text_reviews_count;
 }

 public void setWork_text_reviews_count(String value) {
     work_text_reviews_count = clean(value);
 }

 public String getRatings_1() { return ratings_1; }
 public void setRatings_1(String value) {
     ratings_1 = clean(value);
 }

 public String getRatings_2() { return ratings_2; }
 public void setRatings_2(String value) {
     ratings_2 = clean(value);
 }

 public String getRatings_3() { return ratings_3; }
 public void setRatings_3(String value) {
     ratings_3 = clean(value);
 }

 public String getRatings_4() { return ratings_4; }
 public void setRatings_4(String value) {
     ratings_4 = clean(value);
 }

 public String getRatings_5() { return ratings_5; }
 public void setRatings_5(String value) {
     ratings_5 = clean(value);
 }

 public String getImage_url() { return image_url; }
 public void setImage_url(String value) {
     image_url = clean(value);
 }

 public String getSmall_image_url() { return small_image_url; }
 public void setSmall_image_url(String value) {
     small_image_url = clean(value);
 }


 public String getBookId() {
     return book_id;
 }

 // Convert only when numeric year sorting is needed.
 public Double getPublicationYear() {
     try {
         double year = Double.parseDouble(original_publication_year);

         return Double.isFinite(year)
                 ? Double.valueOf(year)
                 : null;

     } catch (NumberFormatException e) {
         return null;
     }
 }


 public String[] getDataArray() {
     return new String[] {
         book_id, goodreads_book_id, best_book_id, work_id,
         books_count, isbn, isbn13, authors,
         original_publication_year, original_title, title,
         language_code, average_rating, ratings_count,
         work_ratings_count, work_text_reviews_count,
         ratings_1, ratings_2, ratings_3, ratings_4,
         ratings_5, image_url, small_image_url
     };
 }

 @Override
 public String toString() {
     return book_id + " | " + authors + " | " + title
             + " | " + original_publication_year;
 }
}

