<%@page contentType="text/html; charset=UTF-8" %>
<%@page import="at.ac.tuwien.big.we15.lab2.api.DisplayAnswer" %>
<jsp:useBean id="leadingPlayer" scope="session" class="at.ac.tuwien.big.we15.lab2.api.User" />
<jsp:useBean id="secondPlayer"  scope="session" class="at.ac.tuwien.big.we15.lab2.api.User" />
<jsp:useBean id="playerIsLeading" scope="session" type="java.lang.Boolean" />
<jsp:useBean id="gameState" scope="session" type="at.ac.tuwien.big.we15.lab2.api.GameState" />
<jsp:useBean id="question" scope="request" type="at.ac.tuwien.big.we15.lab2.api.DisplayQuestion" />
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="de" lang="de">
    <head>
        <meta charset="utf-8"/>
        <meta name="viewport" content="width=device-width, initial-scale=1.0" />
        <title>Business Informatics Group Jeopardy! - Fragenbeantwortung</title>
        <link rel="stylesheet" type="text/css" href="style/base.css" />
        <link rel="stylesheet" type="text/css" href="style/screen.css" />
        <script src="js/jquery.js" type="text/javascript"></script>
        <script src="js/framework.js" type="text/javascript"></script>
    </head>
    <body id="questionpage">
      <a class="accessibility" href="#questions">Zur Fragenauswahl springen</a>
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
				<li><a class="orangelink navigationlink" id="logoutlink" title="Klicke hier um dich abzumelden" href="login.xhtml" accesskey="l">Abmelden</a></li>
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
      <section id="question" aria-labelledby="questionheading">
            <form id="questionform" action="jeopardy" method="get">
               <h2 id="questionheading" class="accessibility">Frage</h2>
               <p id="questiontype"><%= question.getCategoryName() %> für € <%= question.getValue() %></p>
               <p id="questiontext"><%= question.getQuestionText() %></p>
               <ul id="answers">
<% for(DisplayAnswer ans : question.getAnswers()) { %>
                  <li><input name="answers" id="answer_<%= ans.getId() %>" value="<%= ans.getId() %>" type="checkbox"/><label class="tile clickable" for="answer_<%= ans.getId() %>"><%= ans.getAnswer() %></label></li>
<% } %>
               </ul>
               <input id="timeleftvalue" type="hidden" value="100"/>
               <input class="greenlink formlink clickable" name="answer_submit" id="next" type="submit" value="antworten" accesskey="s"/>
            </form>
         </section>
            
         <section id="timer" aria-labelledby="timerheading">
            <h2 id="timerheading" class="accessibility">Timer</h2>
            <p><span id="timeleftlabel">Verbleibende Zeit:</span> <time id="timeleft">00:30</time></p>
            <meter id="timermeter" min="0" low="20" value="100" max="100"/>
         </section>
      </div>

      <!-- footer -->
      <footer role="contentinfo">© 2015 BIG Jeopardy!</footer>
        
      <script type="text/javascript">
            //<![CDATA[
            
            // initialize time
            $(document).ready(function() {
                var maxtime = 30;
                var hiddenInput = $("#timeleftvalue");
                var meter = $("#timer meter");
                var timeleft = $("#timeleft");
                
                hiddenInput.val(maxtime);
                meter.val(maxtime);
                meter.attr('max', maxtime);
                meter.attr('low', maxtime/100*20);
                timeleft.text(secToMMSS(maxtime));
            });
            
            // update time
            function timeStep() {
                var hiddenInput = $("#timeleftvalue");
                var meter = $("#timer meter");
                var timeleft = $("#timeleft");
                
                var value = $("#timeleftvalue").val();
                if(value > 0) {
                    value = value - 1;   
                }
                
                hiddenInput.val(value);
                meter.val(value);
                timeleft.text(secToMMSS(value));
                
                if(value <= 0) {
                    $('#questionform').submit();
                }
            }
            
            window.setInterval(timeStep, 1000);
            
            //]]>
        </script>
    </body>
</html>
