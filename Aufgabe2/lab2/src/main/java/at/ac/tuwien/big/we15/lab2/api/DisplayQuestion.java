package at.ac.tuwien.big.we15.lab2.api;

import java.util.List;

/**
 * For display of a question in the question view.
 */
public class DisplayQuestion {

	private String categoryName;
	private int value;
	private String questionText;
	private List<DisplayAnswer> answers;

	public DisplayQuestion(String categoryName, int value, String questionText,
			List<DisplayAnswer> answers) {
		super();
		this.categoryName = categoryName;
		this.value = value;
		this.questionText = questionText;
		this.answers = answers;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public String getQuestionText() {
		return questionText;
	}

	public void setQuestionText(String questionText) {
		this.questionText = questionText;
	}

	public List<DisplayAnswer> getAnswers() {
		return answers;
	}

	public void setAnswers(List<DisplayAnswer> answers) {
		this.answers = answers;
	}
}
