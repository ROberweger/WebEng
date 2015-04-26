package at.ac.tuwien.big.we15.lab2.api;

import java.util.List;

/**
 * Class for simulating the pc question selection ans answering
 *
 */

public class PCQuestionSelection {

	private List<Category> categories;

	/**
	 * sets the available categories for chose questions
	 * 
	 * @param categories
	 *            list of available categories
	 */
	public void setCategoryList(List<Category> categories) {
		this.categories = categories;
	}

	/**
	 * question selection for the pc
	 * 
	 * @param dispCategories
	 *            displayed categories
	 * @return
	 */
	public void  selectQuestion(List<DisplayCategory> dispCategories,
			GameState state) {
		DisplayValue actVal = null;
		int catRange = dispCategories.size() - 1;
		boolean selected = false;
		int catIndex = (int) (Math.random() * catRange);

		// iterate over all categories,
		// for (int i = 0; i <= catRange && !selected; i++) {
		// if there are unselected values, select one randomly
		if (dispCategories.get(catIndex).hasUnselectedValues()) {

			List<DisplayValue> vals = dispCategories.get(catIndex)
					.getAvailableSelectValues();

			int valRange = vals.size() - 1;
			int randomValId = (int) (Math.random() * valRange);
			
			actVal = vals.get(randomValId);

			actVal.setChosen(true);
			List<Question> questions = categories.get(catIndex).getQuestions();

			for (Question quest : questions) {
				if (quest.getId() == actVal.getId()) {

					int[] answerIDs = new int[quest.getCorrectAnswers().size()];

					boolean correct = checkAnswers(
							quest.getCorrectAnswers(),
							selectAnswer(quest.getAllAnswers(), quest.getCorrectAnswers(), quest
									.getCorrectAnswers().size(), answerIDs));
					selected = true;

					state.setCategoryChosenByOpponent(quest.getCategory()
							.getName());
					state.setValueOfChosenQuestion(quest.getValue());
					if (correct) {
						state.setChangeOfPrizeOpponent("+" + quest.getValue());
					} else {
						state.setChangeOfPrizeOpponent("-" + quest.getValue());
					}
					state.setIsOpponentAnswerRight(correct);
					break;
				}
			}
		}
		// }
	}

	/**
	 * selects the right number of answers
	 * 
	 * @param answers
	 *            all possibly answers
	 * @param count
	 *            number of correct answers
	 * @param answerID
	 *            stores the selected answer id's
	 * @return
	 */
	private int[] selectAnswer(List<Answer> answers, List<Answer> correct, int count, int[] answerID) {
		if (count > 0) {
			double randomSelect;
			int addID;

			do {
				randomSelect = Math.random();
				if(randomSelect >= 0.6){
					randomSelect *= (correct.size()-1);
					addID = correct.get((int)randomSelect).getId();
				}else{
					randomSelect *= (answers.size()-1);
					addID = answers.get((int)randomSelect).getId();
				}
			} while (answerID.toString().contains(addID+""));

			answerID[count - 1] = addID;
			return selectAnswer(answers, correct, count - 1, answerID);
		}

		return answerID;
	}

	/**
	 * iterate for all selected answers over all correct answers and check if
	 * all are correct
	 * 
	 * @param correct
	 *            all correct answers
	 * @param selectedAnswers
	 *            the id's of the selected answers
	 * @return true if all answers are correct, otherwise false
	 */
	private boolean checkAnswers(List<Answer> correct, int[] selectedAnswers) {

		boolean allCorrect = true;

		for (int i : selectedAnswers) {
			boolean isCorrect = false;

			for (Answer ans : correct) {
				if (ans.getId() == i) {
					isCorrect = true;
					break;
				}
			}

			allCorrect = allCorrect && isCorrect;

		}
		return allCorrect;

	}
}
