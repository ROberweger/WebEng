package twitter;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

public class TwitterService implements ITwitterClient {

	public static final TwitterService INSTANCE = new TwitterService();

	@Override
	public void publishUuid(TwitterStatusMessage message) throws Exception {
		Twitter twitter = TwitterFactory.getSingleton();
		twitter.setOAuthConsumer("GZ6tiy1XyB9W0P4xEJudQ", "gaJDlW0vf7en46JwHAOkZsTHvtAiZ3QUd2mD1x26J9w");
		
		AccessToken accessToken = new AccessToken("1366513208-MutXEbBMAVOwrbFmZtj1r4Ih2vcoHGHE2207002", "RMPWOePlus3xtURWRVnv1TgrjTyK7Zk33evp4KKyA");
        twitter.setOAuthAccessToken(accessToken);
        
        twitter.updateStatus(message.getTwitterPublicationString());
	}

}
