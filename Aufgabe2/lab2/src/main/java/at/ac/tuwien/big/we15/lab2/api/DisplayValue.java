package at.ac.tuwien.big.we15.lab2.api;

/**
 * For displaying the chooseable question in the overview.
 *
 */
public class DisplayValue {

	/**
	 * Id of the question
	 */
	private int id;
	/**
	 * Value of the question
	 */
	private int value;
	/**
	 * true if it was already chosen, false otherwise.
	 */
	private boolean chosen;
	
	public DisplayValue(int id, int value, boolean chosen) {
		super();
		this.id = id;
		this.value = value;
		this.chosen = chosen;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public boolean isChosen() {
		return chosen;
	}

	public void setChosen(boolean chosen) {
		this.chosen = chosen;
	}
}
