package controllers;

import play.mvc.Http.Context;
import play.mvc.Result;
import play.mvc.Security;

public class Secure extends Security.Authenticator {

	@Override
    public String getUsername(Context ctx) {
        return ctx.session().get(Jeopardy.SESSION_USERNAME);
    }

    @Override
    public Result onUnauthorized(Context ctx) {
        return redirect(routes.Jeopardy.authentication());
    }
}
