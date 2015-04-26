package at.ac.tuwien.big.we15.lab2.servlet;

import at.ac.tuwien.big.we15.lab2.api.*;
import at.ac.tuwien.big.we15.lab2.api.impl.ServletJeopardyFactory;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class BigJeopardyServlet extends HttpServlet {
    private User personUser;
    private User pcUser;
    private QuestionDataProvider jsonQuestionDataProvider;
    private List<Category> categories;
    private List<DisplayCategory> displayCategories;
    private List <DisplayValue> values;
    private List <DisplayQuestion> questions;

    private PCQuestionSelection oppunentSelection;
    private GameState state;


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(true);
        RequestDispatcher dispatcher;
        //Get game from jsp
        setState(session.getAttribute("gameState"));
        displayCategories = state.getDisplayCategories();
        if(state.getIsPlayerLeading()){
            personUser = state.getLeadingPlayer();
            pcUser = state.getSecondPlayer();
        }
        else{
            personUser = state.getSecondPlayer();
            pcUser = state.getLeadingPlayer();
        }
        Object questiontype = session.getAttribute("question");
        String[] answers = req.getParameterValues("answers");

        //Points
        if(answers != null && questiontype != null) {
            personUser.setCurrentPrize(personUser.getCurrentPrize() + correctAnswer(questiontype, answers));
        }
        state.setPlayer(personUser, pcUser);
        if (state.getQuestionCountPlayer() < 10) {
            //PC Selection
            if (state.getIsPlayerLeading()&&state.getQuestionCountPlayer()==state.getQuestionCountPC()) {
                state.setQuestionCountPC(state.getQuestionCountPC()+1);
                oppunentSelection.selectQuestion(displayCategories, state);
                if (state.getIsOpponentAnswerRight()) {
                    pcUser.setCurrentPrize(pcUser.getCurrentPrize() + state.getValueOfChosenQuestion());
                } else {
                    pcUser.setCurrentPrize(pcUser.getCurrentPrize() - state.getValueOfChosenQuestion());
                }
            }
            state.setPlayer(personUser, pcUser);
            session.setAttribute("gameState", state);
            session.setAttribute("userPlayer", personUser);
            session.setAttribute("oppunentPlayer", pcUser);
            session.setAttribute("categories", displayCategories);

            dispatcher = getServletContext()
                    .getRequestDispatcher("/jeopardy.jsp");
            dispatcher.forward(req, resp);
        } else {
            state.setPlayer(personUser, pcUser);
            session.setAttribute("userPlayer", personUser);
            session.setAttribute("oppunentPlayer", pcUser);
            session.setAttribute("gameState", state);
            dispatcher = getServletContext()
                    .getRequestDispatcher("/winner.jsp");
            dispatcher.forward(req, resp);
        }
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        RequestDispatcher dispatcher;
        HttpSession session = req.getSession(true);
        if ((req.getParameter("login")!= null)){

            //at the beginning init oppunentSelection;
            oppunentSelection = new PCQuestionSelection();
            personUser = new User();
            personUser.setAvatar(Avatar.getRandomAvatar());
            personUser.setCurrentPrize(0);
            pcUser = new User();
            pcUser.setAvatar(Avatar.getRandomAvatar());
            pcUser.setCurrentPrize(0);
            state = new GameState();


            if(categories == null|| displayCategories == null){
                if (jsonQuestionDataProvider == null) {
                    ServletJeopardyFactory servletJeopardyFactory = new ServletJeopardyFactory(this.getServletContext());
                    jsonQuestionDataProvider = servletJeopardyFactory.createQuestionDataProvider();
                }
                categories = jsonQuestionDataProvider.getCategoryData();
                startCategoriesValue(categories);
            }
            else{
                setAllDisplayCathegories(false);
            }
            // also set the categories for the question selection of the oppunent
            oppunentSelection.setCategoryList(categories);
            state.setDisplayCategories(displayCategories);
            state.setPlayer(personUser, pcUser);
            session.setAttribute("userPlayer", personUser);
            session.setAttribute("oppunentPlayer", pcUser);
            session.setAttribute("gameState", state);
            session.setAttribute("categories", displayCategories);
            dispatcher = getServletContext()
                    .getRequestDispatcher("/jeopardy.jsp");
            dispatcher.forward(req, resp);
        }

        if((req.getParameter("question_submit")!=null)) {

            setState(session.getAttribute("gameState"));
            displayCategories = state.getDisplayCategories();
            if(state.getIsPlayerLeading()){
                personUser = state.getLeadingPlayer();
                pcUser = state.getSecondPlayer();
            }
            else{
                personUser = state.getSecondPlayer();
                pcUser = state.getLeadingPlayer();
            }
            int idValue = 0;
            DisplayQuestion displayQuestion = null;
            Object selectedId = req.getParameter("question_selection");
            if (selectedId != null) {
                idValue = Integer.parseInt(selectedId.toString());
                setVisibleCategories(idValue);
                displayQuestion = getQuestionId(idValue);

            }
            if(displayQuestion != null) {
                state.setQuestionCountPlayer(state.getQuestionCountPlayer() + 1);
                if (state.getQuestionCountPC() < state.getQuestionCountPlayer()) {
                    state.setQuestionCountPC(state.getQuestionCountPC()+1);
                    oppunentSelection.selectQuestion(displayCategories, state);
                    if (state.getIsOpponentAnswerRight()) {
                        pcUser.setCurrentPrize(pcUser.getCurrentPrize() + state.getValueOfChosenQuestion());
                    } else {
                        pcUser.setCurrentPrize(pcUser.getCurrentPrize() - state.getValueOfChosenQuestion());
                    }
                }
                state.setPlayer(personUser, pcUser);
                session.setAttribute("userPlayer", personUser);
                session.setAttribute("oppunentPlayer", pcUser);
                session.setAttribute("gameState", state);
                session.setAttribute("questioncategory", displayCategories);
                session.setAttribute("question", displayQuestion);
                req.setAttribute("question", displayQuestion);
                dispatcher = getServletContext()
                        .getRequestDispatcher("/question.jsp");
                dispatcher.forward(req, resp);
            }
            else{
                dispatcher = getServletContext()
                        .getRequestDispatcher("/jeopardy.jsp");
                dispatcher.forward(req, resp);
            }
        }
        else{
            if(req.getParameter("restart") != null) {

                setState(session.getAttribute("gameState"));
                displayCategories = state.getDisplayCategories();
                if(state.getIsPlayerLeading()){
                    personUser = state.getLeadingPlayer();
                    pcUser = state.getSecondPlayer();
                }
                else{
                    personUser = state.getSecondPlayer();
                    pcUser = state.getLeadingPlayer();
                }
                setAllDisplayCathegories(false);
                state = new GameState();
                personUser.setCurrentPrize(0);
                pcUser.setCurrentPrize(0);
                state.setPlayer(personUser, pcUser);
                state.setDisplayCategories(displayCategories);
                session.setAttribute("userPlayer", personUser);
                session.setAttribute("oppunentPlayer", pcUser);
                session.setAttribute("gameState", state);
                session.setAttribute("categories", displayCategories);
                dispatcher = getServletContext()
                        .getRequestDispatcher("/jeopardy.jsp");
                dispatcher.forward(req, resp);
            }
        }

    }

    // region private Methoden

    /**
     * Speicher alle Kategorien in eine neue Liste displayCategories
     * @param categories
     */
    private void startCategoriesValue(List<Category> categories){
        displayCategories = new LinkedList<>();
        for(Category c: categories){
            values = new LinkedList<>();
            questions = new LinkedList<>();
            for (Question q: c.getQuestions()){
                values.add(new DisplayValue(q.getId(),q.getValue(),false));
                List<DisplayAnswer> displayAnswers = new LinkedList<>();
                for (Answer a: q.getAllAnswers()){
                    displayAnswers.add(new DisplayAnswer(a.getId(),a.getText()));
                }
                questions.add(new DisplayQuestion(q.getCategory().getName(),q.getValue(), q.getId(),q.getText(),displayAnswers));
            }
            displayCategories.add(new DisplayCategory(c.getName(),values));
        }
    }

    /**
     * Sucht eine Frage nach ihrer id und gibt diese zurück wenn sie nicht gefunden wird null
     * @param idValue
     * @return
     */
    private DisplayQuestion getQuestionId(int idValue){
        int catId= 0;
        DisplayQuestion displayQuestion =  null;
        for(Category c: categories){
            for(Question q: c.getQuestions()) {
                if (q.getId() == idValue) {
                    List<DisplayAnswer> displayAnswers = new LinkedList<>();
                    for (Answer a : q.getAllAnswers()) {
                        displayAnswers.add(new DisplayAnswer(a.getId(), a.getText()));
                    }

                    displayQuestion = new DisplayQuestion(q.getCategory().getName(), q.getValue(), q.getId(), q.getText(), displayAnswers);
                    displayQuestion = randomQuestionAnswers(displayQuestion);
                    state.setCategoryChosenByOpponent(c.getName());
                    state.setValueOfChosenQuestion(idValue);

                    return displayQuestion;
                }
            }

        }
        return null;
    }

    /**
     * Setzt einen Value auf Chosen true
     * @param valId
     */
    private void setVisibleCategories(int valId){
        for(DisplayCategory dc: displayCategories){
            for(DisplayValue dv: dc.getSelectableValues() ){
                if(dv.getId() == valId){
                    dv.setChosen(true);
                }
            }
        }
    }

    /**
     * Ordnet die Antworten zufällig in eine Liste an.
     * @param displayQuestion
     * @return
     */
    private DisplayQuestion randomQuestionAnswers(DisplayQuestion displayQuestion){

        List<DisplayAnswer> answers = displayQuestion.getAnswers();
        DisplayAnswer [] newAnswers =new  DisplayAnswer[answers.size()];
        int random =(int) (Math.random() * answers.size() + 1);
        for(int i = 0; i < answers.size();i++){
            int pos = (i+random)%answers.size();
            newAnswers[pos] = answers.get(i);
        }
        displayQuestion.setAnswers(Arrays.asList(newAnswers));
        return displayQuestion;
    }

    /**
     * Bekommt ein Question objekt und eine String Array mit den ausgewählten Antworten
     * wenn alle Richtigen Antworten markiert wurden gibt es die Punkte der Frage positiv zurück
     * sonst negativ
     * wenn die Frage nciht gefundne wird 0
     * @param id
     * @param answer
     * @return
     */
    private int correctAnswer(Object id,  String[] answer) {

        if (id instanceof DisplayQuestion) {
            DisplayQuestion disp = (DisplayQuestion)id;
            int questID = disp.getId();
            for (Category c : categories) {
                for (Question q : c.getQuestions()) {
                    if (q.getId() == disp.getId()) {
                        boolean correct = true;
                        if(answer.length == q.getCorrectAnswers().size()){
                            for(String s: answer){
                                if(correct){
                                    correct = isCorrectAnswer(q.getCorrectAnswers(), Integer.parseInt(s));
                                }
                            }
                        }
                        else {
                            correct = false;
                        }
                        if(correct){
                            state.setChangeOfPrizePlayer("+"+q.getValue());
                            state.setIsPlayerAnswerRight(true);
                            return q.getValue();
                        }
                        else{
                            state.setChangeOfPrizePlayer("-"+q.getValue());
                            state.setIsPlayerAnswerRight(false);
                            return q.getValue() *(-1);
                        }

                    }
                }
            }
        }
        return 0;
    }

    /**
     * Überprüft von einer Liste von Antworten ob eine bestimmte Id vorkommt
     * wenn ja gibt sie true zurück sonst false
     * @param answerList
     * @param aId
     * @return
     */
    private boolean isCorrectAnswer(List<Answer> answerList, int aId){
        if(answerList != null && aId != 0){
            for(Answer answer:answerList){
                if(answer.getId() == aId){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Fragt Session nach aktuellen Spiel ab und speichert dieses
     * @param objectState
     */
    private void setState(Object objectState){
        if(objectState != null || objectState instanceof GameState){
            state = (GameState) objectState;
        }
    }

    /**
     * Setzt den paramter für alle displayValues
     * @param isChosen
     */
    private void setAllDisplayCathegories(boolean isChosen){
        for(DisplayCategory displayCategory: displayCategories){
            for(DisplayValue displayValue : displayCategory.getSelectableValues()){
                displayValue.setChosen(isChosen);
            }
        }
    }

    //endregion
}
