<%@page import="at.ac.tuwien.big.we15.lab2.api.DisplayCategory" %>
<%@page import="at.ac.tuwien.big.we15.lab2.api.DisplayValue" %>
<jsp:useBean id="leadingPlayer" scope="session" class="at.ac.tuwien.big.we15.lab2.api.User" />
<jsp:useBean id="secondPlayer"  scope="session" class="at.ac.tuwien.big.we15.lab2.api.User" />
<jsp:useBean id="playerIsLeading" scope="session" type="java.lang.Boolean" />
<jsp:useBean id="gameState" scope="session" type="at.ac.tuwien.big.we15.lab2.api.GameState" />
<jsp:useBean id="categories" scope="session" type="java.util.List<DisplayCategory>" />
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="de" lang="de">
    <head>
        <meta charset="utf-8"/>
        <meta name="viewport" content="width=device-width, initial-scale=1.0" />
        <title>Business Informatics Group Jeopardy! - Fragenauswahl</title>
        <link rel="stylesheet" type="text/css" href="style/base.css" />
        <link rel="stylesheet" type="text/css" href="style/screen.css" />
        <script src="js/jquery.js" type="text/javascript"></script>
        <script src="js/framework.js" type="text/javascript"></script>
   </head>
   <body id="selection-page">
      <a class="accessibility" href="#question-selection">Zur Fragenauswahl springen</a>
      <!-- Header -->
      <header role="banner" aria-labelledby="bannerheading">
         <h1 id="bannerheading">
            <span class="accessibility">Business Informatics Group </span><span class="gametitle">Jeopardy!</span>
         </h1>
      </header>
      
      <!-- Navigation -->
		<nav role="navigation" aria-labelledby="navheading">
			<h2 id="navheading" class="accessibility">Navigation</h2>
			<ul>
				<li><a class="orangelink navigationlink" id="logoutlink" title="Klicke hier um dich abzumelden" href="#" accesskey="l">Abmelden</a></li>
			</ul>
		</nav>
      
      <!-- Content -->
      <div role="main"> 
         <!-- info -->
         <section id="gameinfo" aria-labelledby="gameinfoinfoheading">
            <h2 id="gameinfoinfoheading" class="accessibility">Spielinformationen</h2>
            <section id="firstplayer" class="playerinfo leader" aria-labelledby="firstplayerheading">
               <h3 id="firstplayerheading" class="accessibility">Führender Spieler</h3>
               <img class="avatar" src="img/avatar/<%= leadingPlayer.getAvatar().getImageHead() %>" alt="Spieler-Avatar <%= leadingPlayer.getAvatar().getName() %>" />
               <table>
                  <tr>
                     <th class="accessibility">Spielername</th>
                     <td class="playername"><%= leadingPlayer.getAvatar().getName() %> <%if(playerIsLeading) {%>(Du)<%}%></td>
                  </tr>
                  <tr>
                     <th class="accessibility">Spielerpunkte</th>
                     <td class="playerpoints"><%= leadingPlayer.getCurrentPrize() %> €</td>
                  </tr>
               </table>
            </section>
            <section id="secondplayer" class="playerinfo" aria-labelledby="secondplayerheading">
               <h3 id="secondplayerheading" class="accessibility">Zweiter Spieler</h3>
               <img class="avatar" src="img/avatar/<%= secondPlayer.getAvatar().getImageHead() %>" alt="Spieler-Avatar <%= secondPlayer.getAvatar().getName() %>" />
               <table>
                  <tr>
                     <th class="accessibility">Spielername</th>
                     <td class="playername"><%= secondPlayer.getAvatar().getName() %> <%if(!playerIsLeading) {%>(Du)<%}%></td>
                  </tr>
                  <tr>
                     <th class="accessibility">Spielerpunkte</th>
                     <td class="playerpoints"><%= secondPlayer.getCurrentPrize() %> €</td>
                  </tr>
               </table>
            </section>
            <p id="round">Fragen: <%= gameState.getQuestionCount() %> / 10</p>
         </section>

         <!-- Question -->
         <section id="question-selection" aria-labelledby="questionheading">
            <h2 id="questionheading" class="black accessibility">Jeopardy</h2>
<% if(gameState.getIsPlayerAnswerRight() != null) { %>
            <p class="user-info positive-change">Du hast <%= gameState.getIsPlayerAnswerRight() ? "richtig" : "falsch" %> geantwortet: <%= gameState.getChangeOfPrizePlayer() %> €</p>
<% } %>
<% if(gameState.getIsOpponentAnswerRight() != null) { %>
            <p class="user-info negative-change">Deadpool hat <%= gameState.getIsOpponentAnswerRight() ? "richtig" : "falsch" %> geantwortet: <%= gameState.getChangeOfPrizeOpponent() %> €</p>
<% } %>
<% if(gameState.getCategoryChosenByOpponent() != null) { %>
            <p class="user-info">Deadpool hat <%= gameState.getCategoryChosenByOpponent() %> für € <%= gameState.getValueOfChosenQuestion() %> gewählt.</p>
<% } %>
            <form id="questionform" action="question.xhtml" method="post">
               <fieldset>
               <legend class="accessibility">Fragenauswahl</legend>
               
<% for(DisplayCategory cat : categories) {%>               
			   <section class="questioncategory" aria-labelledby="<%= cat.getName().toLowerCase().replace(" ", "") %>">
                  <h3 id="<%= cat.getName().toLowerCase().replace(" ", "") %>" class="tile category-title"><span class="accessibility">Kategorie: </span><%= cat.getName() %></h3>
                  <ol class="category_questions">
       <% for(DisplayValue val : cat.getSelectableValues()) {%>
                     <li><input name="question_selection" id="question_<%= val.getId() %>" value="<%= val.getId() %>" type="radio" <% if(val.isChosen()) {%> disabled="disabled" <% } %> /><label class="tile clickable" for="question_<%= val.getId() %>">€ <%= val.getValue() %></label></li>
       <% } %>
                  </ol>
               </section>
<% } %>               
               </fieldset>               
               <input class="greenlink formlink clickable" name="question_submit" id="next" type="submit" value="wählen" accesskey="s" />
            </form>
         </section>
         
         <section id="lastgame" aria-labelledby="lastgameheading">
            <h2 id="lastgameheading" class="accessibility">Letztes Spielinfo</h2>
            <p>Letztes Spiel: Nie</p>
         </section>
		</div>
		
      <!-- footer -->
      <footer role="contentinfo">© 2015 BIG Jeopardy!</footer>
	  
	  <script type="text/javascript">
            //<![CDATA[
            
            // initialize time
            $(document).ready(function() {
                // set last game
                if(supportsLocalStorage()) {
	                var lastGameMillis = parseInt(localStorage['lastGame'])
	                if(!isNaN(parseInt(localStorage['lastGame']))){
	                    var lastGame = new Date(lastGameMillis);
	                	$("#lastgame p").replaceWith('<p>Letztes Spiel: <time datetime="'
	                			+ lastGame.toUTCString()
	                			+ '">'
	                			+ lastGame.toLocaleString()
	                			+ '</time></p>')
	                }
            	}
            });            
            //]]>
        </script>
    </body>
</html>
