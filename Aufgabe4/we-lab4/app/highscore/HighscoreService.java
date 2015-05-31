package highscore;

import models.JeopardyGame;
import models.JeopardyUser;
import models.JeopardyUser.Gender;
import play.Logger;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import java.util.Date;
import java.util.GregorianCalendar;

public class HighscoreService {
	public static final HighscoreService INSTANCE = new HighscoreService();
	private ObjectFactory objectFactory = new ObjectFactory();
	private DatatypeFactory datatypeFactory;
	
	public HighscoreService() {
		try {
			datatypeFactory = DatatypeFactory.newInstance();
		} catch (DatatypeConfigurationException e1) {
			Logger.error("DatatypeFactory couldn't be instanciated");
		}
	}

	public String postToScoreboard(JeopardyGame game)
	{
		Logger.info("Post to scoreboard");
		PublishHighScoreService service = new PublishHighScoreService();
		PublishHighScoreEndpoint publishHighScorePort = service.getPublishHighScorePort();
		
		String uuid = null;
		try {
			uuid = publishHighScorePort.publishHighScore(createHighScoreRequest(game));
			Logger.info("Post to Highscore board successful. Returned UUID: " + uuid);
		} catch (Failure e) {
			Logger.error("Post to highscore board failed");
		}
		return uuid;
	}
	
	private HighScoreRequestType createHighScoreRequest(JeopardyGame game)
	{
		HighScoreRequestType highScoreRequestType = objectFactory.createHighScoreRequestType();
		highScoreRequestType.setUserKey("3ke93-gue34-dkeu9");
		UserDataType userData = objectFactory.createUserDataType();
		JeopardyUser userLoser = game.getLoser().getUser();
		UserType player = getUserType(userLoser.getFirstName(), userLoser.getLastName(), userLoser.getBirthDate(), userLoser.getGender(), game.getLoser().getProfit());
		UserType marvin = getUserType("Marvin", "Player", new Date(), Gender.female, game.getMarvinPlayer().getProfit());
		if(game.isHumanPlayer(game.getLoser().getUser()))
		{
			userData.setLoser(player);
			userData.setWinner(marvin);
		}
		else 
		{
			userData.setLoser(marvin);
			userData.setWinner(player);
		}
		highScoreRequestType.setUserData(userData);
		
		return highScoreRequestType;
	}
	
	private UserType getUserType(String firstName, String lastName, Date birthdate, Gender gender, int points)
	{
		UserType user = objectFactory.createUserType();
		user.setFirstName(firstName);
		user.setLastName(lastName);
		GregorianCalendar birthdateCal = new GregorianCalendar();
		if(birthdate != null) {
			birthdateCal.setTime(birthdate);
			user.setBirthDate(datatypeFactory.newXMLGregorianCalendar(birthdateCal));
		}

		user.setGender(GenderType.getByJeopardyGender(gender));
		user.setPoints(points);
		user.setPassword("");
		return user;
	}
}
