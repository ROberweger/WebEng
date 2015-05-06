package controllers;

//IMPORTS KONTROLLIEREN, es kann vorkommen das auf einmal die Views weg sind!!!
//import views.html.[viewname]
import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;
import views.html.jeopardy;
import views.html.login;
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
    	return ok(jeopardy.render(game));
    }
    
    public static Result login(){
    	//https://www.playframework.com/documentation/2.2.x/JavaGuide4
    	//return ok (login.render(Form.form(User.class)));
    }
    
    public static Result authenticate() {
        //https://www.playframework.com/documentation/2.2.x/JavaGuide4
    }
}
