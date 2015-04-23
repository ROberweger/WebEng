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
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class BigJeopardyServlet extends HttpServlet {
    private User personUser;
    private User pcUser;
    private GameState game;
    private QuestionDataProvider jsonQuestionDataProvider;
    List<Category> categories;
    private List<DisplayCategory> displayCategories;
    private List <DisplayValue> values;
    private List <DisplayQuestion> questions;
    State stateServlet;
    private PCQuestionSelection oppunentSelection;

    @Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
     

		RequestDispatcher dispatcher = getServletContext()
				.getRequestDispatcher("/jeopardy.jsp");
		dispatcher.forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		//Neue Session
				HttpSession session = req.getSession(true);
		        RequestDispatcher dispatcher;
		        GameState state = null;
		        Boolean playerLeading;
		        if(stateServlet == null) {
		            stateServlet = State.Chose;
		            
		            //at the beginning init oppunentSelection;
		            oppunentSelection = new PCQuestionSelection();
		            
		            personUser = new User();
		            personUser.setAvatar(Avatar.getRandomAvatar());
		            personUser.setCurrentPrize(0);

		            pcUser = new User();
		            pcUser.setAvatar(Avatar.getRandomAvatar());
		            pcUser.setCurrentPrize(0);
		            session.setAttribute("userPlayer", personUser);
		            session.setAttribute("oppunentPlayer", pcUser);
		            playerLeading = false;
		            session.setAttribute("playerIsLeading", playerLeading);
		            state = new GameState();
		            state.setQuestionCount(0);
		            if (jsonQuestionDataProvider == null) {
		                ServletJeopardyFactory servletJeopardyFactory = new ServletJeopardyFactory(this.getServletContext());
		                jsonQuestionDataProvider = servletJeopardyFactory.createQuestionDataProvider();
		            }
		            categories = jsonQuestionDataProvider.getCategoryData();
		            startCategoriesValue(categories);
		            
		            // also set the categories for the question selection of the oppunent
		            oppunentSelection.setCategoryList(categories);
		            
		            
		            session.setAttribute("gameState", state);
		            session.setAttribute("categories", displayCategories);
		            dispatcher = getServletContext()
		                    .getRequestDispatcher("/jeopardy.jsp");
		            dispatcher.forward(req, resp);

		        }
		        else if(stateServlet ==State.Chose) {
		            stateServlet = State.question;
		            int idCategory = 0;
		            int idValue = 0;
		            if (personUser.getCurrentPrize() <= pcUser.getCurrentPrize()) {
		                Object selectedId = session.getAttribute("questionform");
		                if (selectedId instanceof DisplayValue) {
		                    idValue = (int) ((DisplayValue) selectedId).getId();

		                }
		            } else {
		                //PC Auswahl
		            	oppunentSelection.selectQuestion(displayCategories, state);
		    			if(state.getIsOpponentAnswerRight()){
		    				pcUser.setCurrentPrize(pcUser.getCurrentPrize()+state.getValueOfChosenQuestion());
		    			}else{
		    				pcUser.setCurrentPrize(pcUser.getCurrentPrize()-state.getValueOfChosenQuestion());
		    			}
		            }
		            session.setAttribute("gameState", state);

		            DisplayQuestion displayQuestion = getQuestionId(idCategory, idValue);
		            state.setQuestionCount(state.getQuestionCount() + 1);
		            state.setCategoryChosenByOpponent(displayQuestion.getCategoryName());
		            state.setValueOfChosenQuestion(displayQuestion.getValue());
		            dispatcher = getServletContext()
		                    .getRequestDispatcher("/question.jsp");
		            dispatcher.forward(req, resp);
		        }
		        else if(stateServlet == State.question) {

		            Object questiontype = session.getAttribute("questiontype");
		            Object questiontext = session.getAttribute("questiontext");
		            Object answers = session.getAttribute("answers");
		            personUser.setCurrentPrize(personUser.getCurrentPrize() + correctAnswer(questiontype, questiontext, answers));

		            if (personUser.getCurrentPrize() > pcUser.getCurrentPrize()) {
		                playerLeading = true;
		            }
		            if (state.getQuestionCount() < 10) {
		                session.setAttribute("gameState", state);
		                session.setAttribute("questioncategory", displayCategories);
		                dispatcher = getServletContext()
		                        .getRequestDispatcher("/jeopardy.jsp");
		                dispatcher.forward(req, resp);
		            } else {
		                session.setAttribute("gameState", state);
		                dispatcher = getServletContext()
		                        .getRequestDispatcher("/winner.jsp");
		                dispatcher.forward(req, resp);
		            }
		        }
		
	}
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
                questions.add(new DisplayQuestion(q.getCategory().getName(),q.getValue(),q.getText(),displayAnswers));
            }
            displayCategories.add(new DisplayCategory(c.getName(),values));
        }
    }
    private DisplayQuestion getQuestionId(int idDC, int idValue){
        Category c = categories.get(idDC);
        DisplayQuestion displayQuestion =  null;
        for(Question q: c.getQuestions()){
            if(q.getId() == idValue){
                List<DisplayAnswer> displayAnswers = new LinkedList<>();
                for (Answer a: q.getAllAnswers()){
                    displayAnswers.add(new DisplayAnswer(a.getId(),a.getText()));
                }

                displayQuestion = new DisplayQuestion(q.getCategory().getName(),q.getValue(),q.getText(),displayAnswers);
                displayQuestion= randomQuestionAnswers(displayQuestion);
            }
        }
        DisplayValue displayValue = displayCategories.get(idDC).getSelectableValues().get(idValue);
        displayCategories.get(idDC).getSelectableValues().remove(displayValue);
        displayValue.setChosen(false);
        displayCategories.get(idDC).getSelectableValues().set(idValue,displayValue);
        return  displayQuestion;
    }
    private DisplayQuestion randomQuestionAnswers(DisplayQuestion displayQuestion){
        Random r = new Random();
        List<DisplayAnswer> answers = displayQuestion.getAnswers();
        List<DisplayAnswer> newAnswers = new LinkedList<>();
        int random = r.nextInt()*answers.size();
        for(int i = 0; i < answers.size();i++){
            int pos = (i+random)%answers.size();
            newAnswers.set(pos,answers.get(i));
        }
        displayQuestion.setAnswers(newAnswers);
        return displayQuestion;
    }
    private int correctAnswer(Object category, Object text, Object answer) {
        if (category instanceof Category) {
            int i = 0;
            for (DisplayCategory c : displayCategories) {
                i++;
                if (c.getName().equals(((Category) category).getName()))
                    for (Question q : categories.get(i).getQuestions()) {
                        if (q.getText().equals(text.toString())) {
                            q.getCorrectAnswers().equals(answer);
                            return q.getValue();
                        } else {
                            return 0;
                        }
                    }
            }
        }
        return 0;
    }
}
