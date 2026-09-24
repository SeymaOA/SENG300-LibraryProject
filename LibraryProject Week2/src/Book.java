package Week2LibraryProjectV3;

public class Book {
	
	private String[] data;
	
	public Book (String[] data) {
		this.data = data;
	}
	
	public String getBookId() {
		return data[0];
	}
	
	public String getIsbn() {
		return data[5];
	}
	
	public String getAuthors() {
		return data[7];
	}
	
	public double getPublicationYear() {
		try {
			return Double.parseDouble(data[8]);
		}
		
		catch (NumberFormatException | NullPointerException e) {
			return 0.0;
		}
	}
	
	public String[] getDataArray() {
        return data;
    }
}
