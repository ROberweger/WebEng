package controllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

import models.Login;
import at.ac.tuwien.big.we15.lab2.api.JeopardyFactory;
import at.ac.tuwien.big.we15.lab2.api.JeopardyGame;
import at.ac.tuwien.big.we15.lab2.api.impl.PlayJeopardyFactory;
import play.api.i18n.Lang;
import play.cache.Cache;
import play.data.DynamicForm;
import play.data.Form;
import play.i18n.Messages;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.authentication;
import views.html.jeopardy;
import views.html.registration;
import views.html.question;
import views.html.winner;

public class Jeopardy extends Controller {
	//Parameter names
	public static final String SESSION_USERNAME = "user";
	public static final String CACHE_GAME = "game";

	public static Result registration() {
		return ok(registration.render());
	}
	
	public static Result authentication() {
		return ok(authentication.render());
	}
	
	public static Result login() {
		DynamicForm form = Form.form().bindFromRequest();
		String username = form.get("username");
		String password = form.get("password");
		//TODO: check login
		
		//If login is valid -> set username in session
		session(SESSION_USERNAME, username);
		return redirect(routes.Jeopardy.jeopardyGet());
	}
	
	@Security.Authenticated(Secure.class)
	public static Result logout() {
		session().remove(SESSION_USERNAME);
		return redirect(routes.Jeopardy.authentication());
	}
	
	@Security.Authenticated(Secure.class)
	public static Result jeopardyGet() {
		JeopardyFactory factory = new PlayJeopardyFactory("data." + lang().code() + ".json");
		JeopardyGame game = factory.createGame(session(SESSION_USERNAME));
		Cache.set(CACHE_GAME, game);
		return ok(jeopardy.render(game));
	}
	
	@Security.Authenticated(Secure.class)
	public static Result jeopardyPost() {
		DynamicForm form = Form.form().bindFromRequest();
		//Restart is the same as jeopardyGet
		if(form.get("restart") != null)
			return jeopardyGet();
		
		JeopardyGame game = (JeopardyGame) Cache.get(CACHE_GAME);
		String[] answersStr = request().body().asFormUrlEncoded().get("answers");
		ArrayList<Integer> answers = new ArrayList<Integer>();
		if(answersStr != null) {
			for(String ans : answersStr)
				answers.add(Integer.valueOf(ans));
		}
		game.answerHumanQuestion(answers);
		
		if(game.isGameOver())
			return ok(winner.render(game));
		else
			return ok(jeopardy.render(game));
	}
	
	@Security.Authenticated(Secure.class)
	public static Result question() {
		DynamicForm selectionform = Form.form().bindFromRequest();
		JeopardyGame game = (JeopardyGame) Cache.get(CACHE_GAME);
		String selectedQuestion = selectionform.get("question_selection");
		//If no question was selected, render jeopardy again
		if(selectedQuestion == null)
			return ok(jeopardy.render(game));
		
		game.chooseHumanQuestion(Integer.valueOf(selectedQuestion));
		return ok(question.render(game));
	}
}
