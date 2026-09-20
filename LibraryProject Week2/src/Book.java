
package Week2LibraryProject;

//Data model
//There are total 23 coloumns in database
// this is base class
public class Book {
	
	private String book_id;
	private String goodreads_book_id;
	private String best_book_id; //check what is max number. if it fits to int, we can use int because its saves space
	private String work_id;
	private String books_count;
	private String isbn;
	private String isbn13;
	private String authors;
	private String original_publication_year; //try with import java.time.Year;  // private Year original_publication_year;
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
	
	public Book(String[] fields) {     //Constructor
		book_id = fields[0];
		goodreads_book_id = fields[1];
		best_book_id = fields[2];
		work_id = fields[3];
		books_count = fields[4];
		isbn = fields[5];
		isbn13 = fields[6];
		authors = fields[7];
		original_publication_year = fields[8];
		original_title = fields[9];
		title = fields[10];
		language_code = fields[11];
		average_rating = fields[12];
		ratings_count = fields[13];
		work_ratings_count = fields[14];
		work_text_reviews_count = fields[15];
		ratings_1 = fields[16];
		ratings_2 = fields[17];
		ratings_3 = fields[18]; 
		ratings_4 = fields[19];
		ratings_5 = fields[20];
		image_url = fields[21];
		small_image_url = fields[22];

	}
	
	
	public String getBook_id() {
		return book_id;
	}
	public void setBook_id(String book_id) {
		this.book_id = book_id;
	}
	public String getGoodreads_book_id() {
		return goodreads_book_id;
	}
	public void setGoodreads_book_id(String goodreads_book_id) {
		this.goodreads_book_id = goodreads_book_id;
	}
	public String getBest_book_id() {
		return best_book_id;
	}
	public void setBest_book_id(String best_book_id) {
		this.best_book_id = best_book_id;
	}
	public String getWork_id() {
		return work_id;
	}
	public void setWork_id(String work_id) {
		this.work_id = work_id;
	}
	public String getBooks_count() {
		return books_count;
	}
	public void setBooks_count(String books_count) {
		this.books_count = books_count;
	}
	public String getIsbn() {
		return isbn;
	}
	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}
	public String getIsbn13() {
		return isbn13;
	}
	public void setIsbn13(String isbn13) {
		this.isbn13 = isbn13;
	}
	public String getAuthors() {
		return authors;
	}
	public void setAuthors(String authors) {
		this.authors = authors;
	}
	public String getOriginal_publication_year() {
		return original_publication_year;
	}
	public void setOriginal_publication_year(String original_publication_year) {
		this.original_publication_year = original_publication_year;
	}
	public String getOriginal_title() {
		return original_title;
	}
	public void setOriginal_title(String original_title) {
		this.original_title = original_title;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getLanguage_code() {
		return language_code;
	}
	public void setLanguage_code(String language_code) {
		this.language_code = language_code;
	}
	public String getAverage_rating() {
		return average_rating;
	}
	public void setAverage_rating(String average_rating) {
		this.average_rating = average_rating;
	}
	public String getRatings_count() {
		return ratings_count;
	}
	public void setRatings_count(String ratings_count) {
		this.ratings_count = ratings_count;
	}
	public String getWork_ratings_count() {
		return work_ratings_count;
	}
	public void setWork_ratings_count(String work_ratings_count) {
		this.work_ratings_count = work_ratings_count;
	}
	public String getWork_text_reviews_count() {
		return work_text_reviews_count;
	}
	public void setWork_text_reviews_count(String work_text_reviews_count) {
		this.work_text_reviews_count = work_text_reviews_count;
	}
	public String getRatings_1() {
		return ratings_1;
	}
	public void setRatings_1(String ratings_1) {
		this.ratings_1 = ratings_1;
	}
	public String getRatings_2() {
		return ratings_2;
	}
	public void setRatings_2(String ratings_2) {
		this.ratings_2 = ratings_2;
	}
	public String getRatings_3() {
		return ratings_3;
	}
	public void setRatings_3(String ratings_3) {
		this.ratings_3 = ratings_3;
	}
	public String getRatings_4() {
		return ratings_4;
	}
	public void setRatings_4(String ratings_4) {
		this.ratings_4 = ratings_4;
	}
	public String getRatings_5() {
		return ratings_5;
	}
	public void setRatings_5(String ratings_5) {
		this.ratings_5 = ratings_5;
	}
	public String getImage_url() {
		return image_url;
	}
	public void setImage_url(String image_url) {
		this.image_url = image_url;
	}
	public String getSmall_image_url() {
		return small_image_url;
	}
	public void setSmall_image_url(String small_image_url) {
		this.small_image_url = small_image_url;
	}
	@Override
	public String toString() {
		return book_id + " " + authors  + " " + title ;
	}
	
}
