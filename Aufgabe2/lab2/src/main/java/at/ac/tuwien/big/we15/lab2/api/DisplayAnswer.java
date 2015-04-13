package at.ac.tuwien.big.we15.lab2.api;

/**
 * For diplay of selectable answers in the question view.
 */
public class DisplayAnswer {

	private int id;
	private String answer;

	public DisplayAnswer(int id, String answer) {
		super();
		this.id = id;
		this.answer = answer;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

}
