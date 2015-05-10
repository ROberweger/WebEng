package controllers;

import at.ac.tuwien.big.we15.lab2.api.JeopardyFactory;
import at.ac.tuwien.big.we15.lab2.api.JeopardyGame;
import at.ac.tuwien.big.we15.lab2.api.impl.PlayJeopardyFactory;
import models.Registration;
import play.cache.Cache;
import play.data.DynamicForm;
import play.data.Form;
import play.db.jpa.JPA;

import play.db.jpa.Transactional;
import play.i18n.Messages;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;

import javax.persistence.EntityManager;
import java.util.ArrayList;

public class Jeopardy extends Controller {
	//Parameter names
	public static final String SESSION_USERNAME = "user";
	public static final String CACHE_GAME = "game";

	@play.db.jpa.Transactional
	public static Result registration() {
		Form<Registration> form = Form.form(Registration.class).bindFromRequest();
		if (form.hasErrors()) {
			return badRequest(views.html.registration.render(form));
		} else {
			Registration registration = form.get();
			if(getRegistratonByUserName(registration.getUserName())== null) {
				JPA.em().persist(registration);
				return authentication();
			}
			else{
				//return forbidden(routes.Jeopardy.registration());
				return forbidden("Bitte geben sie einen anderen Username an");
			}
		}
	}
	public static Result registrationRender(){
		return ok(views.html.registration.render(Form.form(Registration.class)));
	}
	
	public static Result authentication() {
		return ok(views.html.authentication.render());
	}
	
	public static Result login() {
		DynamicForm form = Form.form().bindFromRequest();
		String username = form.get("username");
		String password = form.get("password");
		//TODO: check login
		Registration r = getRegistratonByUserName(username);
		if((r != null) &&(r.comparePassword(password))){
			session(SESSION_USERNAME, username);
			return redirect(routes.Jeopardy.jeopardyGet());
		}
		else {
			return unauthorized("Falscher Username oder Passwort");
		}

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
		return ok(views.html.jeopardy.render(game));
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
			return ok(views.html.winner.render(game));
		else
			return ok(views.html.jeopardy.render(game));
	}
	
	@Security.Authenticated(Secure.class)
	public static Result question() {
		DynamicForm selectionform = Form.form().bindFromRequest();
		JeopardyGame game = (JeopardyGame) Cache.get(CACHE_GAME);
		String selectedQuestion = selectionform.get("question_selection");
		//If no question was selected, render jeopardy again
		if(selectedQuestion == null)
			return ok(views.html.jeopardy.render(game));
		
		game.chooseHumanQuestion(Integer.valueOf(selectedQuestion));
		return ok(views.html.question.render(game));
	}

	@play.db.jpa.Transactional
	public static Registration getRegistratonByUserName(String userName) {
		Registration r = findByUsername(userName);
		return r;
	}

	private static Registration findByUsername(String userName) {
		EntityManager em = play.db.jpa.JPA.em();
		return em.find(Registration.class, userName);
	}
}
