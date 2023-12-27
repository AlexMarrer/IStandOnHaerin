package Pruefung2;


public class Data {

	private int id;
	private String title;
	private String text;
	
	public Data(int id, String title, String text) {
		super();
		this.id = id;
		this.title = title;
		this.text = text;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	public void getInfo() {
		System.out.println("Id: " + id + "\nTitle: " + title + "\ntext: " + text);
	}
}
