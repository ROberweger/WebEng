package at.ac.tuwien.big.we15.lab2.api;

import java.util.List;

public class GameState {

    /**
     * the player who is leading
     */
    private User leadingPlayer;

    /**
     * the player who is second
     */
    private User secondPlayer;

    /**
     * true if person player is leading, false otherwise
     */
    private boolean isPlayerLeading;

    private int questionCountPlayer;
    private int questionCountPC;
    /**
     * True if the answer of the opponent is right, false otherwise
     */
    private Boolean isOpponentAnswerRight;
    /**
     * Change of the prize after a question, e.g. +1000
     */
    private String changeOfPrizeOpponent;
    /**
     * True if the answer of the player is right, false otherwise
     */
    private Boolean isPlayerAnswerRight;
    /**
     * Change of the prize after a question, e.g. +1000
     */
    private String changeOfPrizePlayer;
    // For display of last chosen question by opponent
    private String categoryChosenByOpponent;
    private int valueOfChosenQuestion;
    private List<DisplayCategory> displayCategories;

    public GameState() {
        super();
        questionCountPlayer = 0;
        questionCountPC = 0;
    }

    public int getQuestionCountPlayer() {
        return questionCountPlayer;
    }

    public void setQuestionCountPlayer(int questionCountPlayer) {
        this.questionCountPlayer = questionCountPlayer;
    }

    public Boolean getIsOpponentAnswerRight() {
        return isOpponentAnswerRight;
    }

    public void setIsOpponentAnswerRight(Boolean isOpponentAnswerRight) {
        this.isOpponentAnswerRight = isOpponentAnswerRight;
    }

    public Boolean getIsPlayerAnswerRight() {
        return isPlayerAnswerRight;
    }

    public void setIsPlayerAnswerRight(Boolean isPlayerAnswerRight) {
        this.isPlayerAnswerRight = isPlayerAnswerRight;
    }

    public String getCategoryChosenByOpponent() {
        return categoryChosenByOpponent;
    }

    public void setCategoryChosenByOpponent(String categoryChosenByOpponent) {
        this.categoryChosenByOpponent = categoryChosenByOpponent;
    }

    public int getValueOfChosenQuestion() {
        return valueOfChosenQuestion;
    }

    public void setValueOfChosenQuestion(int valueOfChosenQuestion) {
        this.valueOfChosenQuestion = valueOfChosenQuestion;
    }

    public String getChangeOfPrizeOpponent() {
        return changeOfPrizeOpponent;
    }

    public void setChangeOfPrizeOpponent(String changeOfPrizeOpponent) {
        this.changeOfPrizeOpponent = changeOfPrizeOpponent;
    }

    public String getChangeOfPrizePlayer() {
        return changeOfPrizePlayer;
    }

    public void setChangeOfPrizePlayer(String changeOfPrizePlayer) {
        this.changeOfPrizePlayer = changeOfPrizePlayer;
    }

    /**
     * sets the leading and second player and if the person player is leading or not
     * @param userPlayer the user player object
     * @param oppunentPlayer the pc player object
     */
    public void setPlayer(User userPlayer, User oppunentPlayer){
        if(userPlayer.getCurrentPrize()>oppunentPlayer.getCurrentPrize()){
            leadingPlayer = userPlayer;
            secondPlayer = oppunentPlayer;
            isPlayerLeading = true;
        }else if (userPlayer.getCurrentPrize()<oppunentPlayer.getCurrentPrize()){
            leadingPlayer = oppunentPlayer;
            secondPlayer = userPlayer;
            isPlayerLeading = false;
        }else{
            leadingPlayer = userPlayer;
            secondPlayer = oppunentPlayer;
            isPlayerLeading = false;
        }
    }

    /**
     * returns the leading player of the game
     * @return leading user object
     */
    public User getLeadingPlayer(){
        return leadingPlayer;
    }

    /**
     * returns the second player of the game
     * @return second user object
     */
    public User getSecondPlayer(){
        return secondPlayer;
    }

    /**
     * returns if the person player is leading
     * @return true if person player is leading, false otherwise
     */
    public boolean getIsPlayerLeading(){
        return isPlayerLeading;
    }

    public List<DisplayCategory> getDisplayCategories() {
        return displayCategories;
    }

    public void setDisplayCategories(List<DisplayCategory> displayCategories) {
        this.displayCategories = displayCategories;
    }

    public int getQuestionCountPC() {
        return questionCountPC;
    }

    public void setQuestionCountPC(int questionCountPC) {
        this.questionCountPC = questionCountPC;
    }
}
