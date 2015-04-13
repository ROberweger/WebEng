package at.ac.tuwien.big.we15.lab2.api;

import java.util.List;

/**
 * For display of categories in the overview.
 */
public class DisplayCategory {

	private String name;
	private List<DisplayValue> selectableValues;

	public DisplayCategory(String name, List<DisplayValue> selectableValues) {
		super();
		this.name = name;
		this.selectableValues = selectableValues;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<DisplayValue> getSelectableValues() {
		return selectableValues;
	}

	public void setSelectableValues(List<DisplayValue> selectableValues) {
		this.selectableValues = selectableValues;
	}
}
