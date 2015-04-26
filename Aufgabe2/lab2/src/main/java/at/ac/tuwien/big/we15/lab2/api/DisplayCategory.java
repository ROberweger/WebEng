package at.ac.tuwien.big.we15.lab2.api;

import java.util.ArrayList;
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

    /**
     * checks if there are unselected values in this category
     * @return true if there are unselected values, false otherwise
     */
    public boolean hasUnselectedValues() {
        boolean hasUnselected = false;

        for (DisplayValue val : selectableValues) {
            hasUnselected = hasUnselected || !val.isChosen(); // isChosen true
            // if it was
            // already
            // selected
        }

        return hasUnselected;
    }

    /**
     * creates a list with all selectable values which are available
     * @return list with selectable values
     */
    public List<DisplayValue> getAvailableSelectValues() {
        List<DisplayValue> unselected = new ArrayList<DisplayValue>();

        for (DisplayValue val : selectableValues) {
            if (!val.isChosen()) {
                unselected.add(val);
            }
        }

        return unselected;
    }

}
