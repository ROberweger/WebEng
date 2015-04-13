package at.ac.tuwien.big.we15.lab2.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import at.ac.tuwien.big.we15.lab2.api.Avatar;
import at.ac.tuwien.big.we15.lab2.api.DisplayCategory;
import at.ac.tuwien.big.we15.lab2.api.DisplayValue;
import at.ac.tuwien.big.we15.lab2.api.GameState;
import at.ac.tuwien.big.we15.lab2.api.User;

public class BigJeopardyServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		HttpSession session = req.getSession(true);
		User leading = new User();
		leading.setAvatar(Avatar.CAPTAIN_AMERICA);
		leading.setCurrentPrize(100);
		session.setAttribute("leadingPlayer", leading);
		User second = new User();
		second.setAvatar(Avatar.DOCTOR_OCTOPUS);
		second.setCurrentPrize(50);
		session.setAttribute("secondPlayer", second);
		Boolean playerLeading = false;
		session.setAttribute("playerIsLeading", playerLeading);
		GameState state = new GameState();
		state.setQuestionCount(2);
		state.setIsPlayerAnswerRight(false);
		state.setChangeOfPrizePlayer("+100");
		state.setCategoryChosenByOpponent("Blahu");
		state.setValueOfChosenQuestion(200);
		session.setAttribute("gameState", state);
		List<DisplayCategory> cats = new ArrayList<DisplayCategory>();
		List<DisplayValue> values = new ArrayList<DisplayValue>();
		values.add(new DisplayValue(1, 100, false));
		values.add(new DisplayValue(2, 200, true));
		values.add(new DisplayValue(3, 400, false));
		cats.add(new DisplayCategory("TU WIEN", values));
		session.setAttribute("categories", cats);
		
		RequestDispatcher dispatcher = getServletContext()
				.getRequestDispatcher("/jeopardy.jsp");
		dispatcher.forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		RequestDispatcher dispatcher = getServletContext()
				.getRequestDispatcher("/jeopardy.jsp");
		dispatcher.forward(req, resp);
	}
}
