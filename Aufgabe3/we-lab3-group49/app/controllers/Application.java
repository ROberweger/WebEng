package controllers;

import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;
import at.ac.tuwien.big.we15.lab2.api.JeopardyFactory;
import at.ac.tuwien.big.we15.lab2.api.JeopardyGame;
import at.ac.tuwien.big.we15.lab2.api.impl.PlayJeopardyFactory;

public class Application extends Controller {

    public static Result index() {
        return ok(index.render("Your new application is ready."));
    }

    public static Result jeopardy() {
    	JeopardyFactory factory = new PlayJeopardyFactory("data.de.json");
    	JeopardyGame game = factory.createGame("Testuser");
    	String bla =game.getCategories().get(0).getQuestions().get(0).getText();
    	return ok(bla);
    }
}
