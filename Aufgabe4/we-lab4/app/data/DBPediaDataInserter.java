package data;

import java.util.List;
import java.util.Locale;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;

import play.Logger;
import at.ac.tuwien.big.we.dbpedia.api.DBPediaService;
import at.ac.tuwien.big.we.dbpedia.api.SelectQueryBuilder;
import at.ac.tuwien.big.we.dbpedia.vocabulary.DBPedia;
import at.ac.tuwien.big.we.dbpedia.vocabulary.DBPediaOWL;
import models.Answer;
import models.Category;
import models.Question;

public class DBPediaDataInserter {
	
	
	public static Category insertDBPediaData() throws Exception{
		
		Category music = new Category();
		music.setNameDE("Musik");
		music.setNameEN("Music");
		
		try{
		music.addQuestion(austrianBands());
		music.addQuestion(metalBands());
		music.addQuestion(bandMember());
		music.addQuestion(bandRecordLabel());
		music.addQuestion(austrianBandAndSonyLabel());
		
		
		return music;
		}catch(Exception ex){
			Logger.info(ex.getMessage() +" Couldn't retrive data from DBPedia");
			throw new Exception();
		}
		
	}
	
	//austrian bands
	private static Question austrianBands() throws Exception{
		if(!DBPediaService.isAvailable()){
			Logger.info("DBPedia isn't available!");
			throw new Exception();
		}
		
		Resource austria = DBPediaService.loadStatements(DBPedia.createResource("Austria"));
		
		SelectQueryBuilder musicQuery = DBPediaService.createQueryBuilder()
				.setLimit(3) // at most three statements
				.addWhereClause(RDF.type, DBPediaOWL.Band)
				.addWhereClause(DBPediaOWL.hometown, austria)
				.addFilterClause(RDFS.label, Locale.GERMAN)
				.addFilterClause(RDFS.label, Locale.ENGLISH);
		
		Model austrianBands = DBPediaService.loadStatements(musicQuery.toQueryString());
		
		List<String> englishAustrianBandNames =
				DBPediaService.getResourceNames(austrianBands, Locale.ENGLISH);
		List<String> germanAustrianBandNames =
				DBPediaService.getResourceNames(austrianBands, Locale.GERMAN);
		
		musicQuery.removeWhereClause(DBPediaOWL.hometown, austria);
		musicQuery.addMinusClause(DBPediaOWL.hometown, austria);
		musicQuery.setLimit(5-englishAustrianBandNames.size());
		
		Model noAustrianBands = DBPediaService.loadStatements(musicQuery.toQueryString());
		
		List<String> englishNoAustrianBandNames = DBPediaService.getResourceNames(noAustrianBands, Locale.ENGLISH);
		List<String> germanNoAustrianBandNames = DBPediaService.getResourceNames(noAustrianBands, Locale.GERMAN);
		
		Question austrianBandQuestion = new Question();
		austrianBandQuestion.setTextDE("Das sind Musik-Bands die aus Österreich kommen");
		austrianBandQuestion.setTextEN("These Music-Bands come from Austria");
		
		for(int i = 0; i < englishAustrianBandNames.size(); i++){
			Answer right = new Answer();
			right.setTextDE(replaceBrackets(germanAustrianBandNames.get(i)));
			right.setTextEN(replaceBrackets(englishAustrianBandNames.get(i)));
			austrianBandQuestion.addRightAnswer(right);
		}
		
		for(int i = 0; i < englishNoAustrianBandNames.size(); i++){
			Answer wrong = new Answer();
			wrong.setTextDE(replaceBrackets(germanNoAustrianBandNames.get(i)));
			wrong.setTextEN(replaceBrackets(englishNoAustrianBandNames.get(i)));
			austrianBandQuestion.addWrongAnswer(wrong);
		}
		
		return austrianBandQuestion;
	}
	
	//metal bands
	private static Question metalBands() throws Exception{
		if(!DBPediaService.isAvailable()){
			Logger.info("DBPedia isn't available!");
			throw new Exception();
		}
		Resource genre = DBPediaService.loadStatements(DBPedia.createResource("Heavy_metal_music"));
		
		SelectQueryBuilder musicQuery = DBPediaService.createQueryBuilder()
				.setLimit(3) // at most three statements
				.addWhereClause(RDF.type, DBPediaOWL.Band)
				.addWhereClause(DBPediaOWL.genre, genre)
				.addFilterClause(RDFS.label, Locale.GERMAN)
				.addFilterClause(RDFS.label, Locale.ENGLISH);
		
		Model metalbands = DBPediaService.loadStatements(musicQuery.toQueryString());
		
		List<String> englishMetalBandNames =
				DBPediaService.getResourceNames(metalbands, Locale.ENGLISH);
		List<String> germanMetalBandNames =
				DBPediaService.getResourceNames(metalbands, Locale.GERMAN);
		
		musicQuery.removeWhereClause(DBPediaOWL.genre, genre);
		musicQuery.addMinusClause(DBPediaOWL.genre, genre);
		musicQuery.setLimit(5-englishMetalBandNames.size());
		
		Model bands = DBPediaService.loadStatements(musicQuery.toQueryString());
		
		List<String> englishBandNames = DBPediaService.getResourceNames(bands, Locale.ENGLISH);
		List<String> germanBandNames = DBPediaService.getResourceNames(bands, Locale.GERMAN);
		
		Question metalBandQuestion = new Question();
		metalBandQuestion.setTextDE("Der Musikstil dieser Band ist Metal");
		metalBandQuestion.setTextEN("The music genre of these Band is metal");
		
		
		for(int i = 0; i < englishMetalBandNames.size(); i++){
			Answer right = new Answer();
			right.setTextDE(replaceBrackets(germanMetalBandNames.get(i)));
			right.setTextEN(replaceBrackets(englishMetalBandNames.get(i)));
			metalBandQuestion.addRightAnswer(right);
		}
		
		for(int i = 0; i < englishBandNames.size(); i++){
			Answer wrong = new Answer();
			wrong.setTextDE(replaceBrackets(germanBandNames.get(i)));
			wrong.setTextEN(replaceBrackets(englishBandNames.get(i)));
			metalBandQuestion.addWrongAnswer(wrong);
		}
		
		return metalBandQuestion;
	}
	
	//member of a band 
	private static Question bandMember() throws Exception{
		if(!DBPediaService.isAvailable()){
			Logger.info("DBPedia isn't available!");
			throw new Exception();
		}
		Resource member = DBPediaService.loadStatements(DBPedia.createResource("Corey_Taylor"));
		
		SelectQueryBuilder musicQuery = DBPediaService.createQueryBuilder()
				.setLimit(3) // at most three statements
				.addWhereClause(RDF.type, DBPediaOWL.Band)
				.addWhereClause(DBPediaOWL.bandMember, member)
				.addFilterClause(RDFS.label, Locale.GERMAN)
				.addFilterClause(RDFS.label, Locale.ENGLISH);
		
		Model memberOfBands = DBPediaService.loadStatements(musicQuery.toQueryString());
		
		List<String> englishMemberOfBandNames =
				DBPediaService.getResourceNames(memberOfBands, Locale.ENGLISH);
		List<String> germanMemberOfBandNames =
				DBPediaService.getResourceNames(memberOfBands, Locale.GERMAN);
		
		musicQuery.removeWhereClause(DBPediaOWL.bandMember, member);
		musicQuery.addMinusClause(DBPediaOWL.bandMember, member);
		musicQuery.setLimit(5-englishMemberOfBandNames.size());
		
		Model bands = DBPediaService.loadStatements(musicQuery.toQueryString());
		
		List<String> englishBandNames = DBPediaService.getResourceNames(bands, Locale.ENGLISH);
		List<String> germanBandNames = DBPediaService.getResourceNames(bands, Locale.GERMAN);
		
		Question bandMemberQuestion = new Question();
		bandMemberQuestion.setTextDE("Corey Taylor ist Mitglied bei diesen Bands");
		bandMemberQuestion.setTextEN("Corey Taylor is a member of this bands");
		
		for(int i = 0; i < englishMemberOfBandNames.size(); i++){
			Answer right = new Answer();
			right.setTextDE(replaceBrackets(germanMemberOfBandNames.get(i)));
			right.setTextEN(replaceBrackets(englishMemberOfBandNames.get(i)));
			bandMemberQuestion.addRightAnswer(right);
		}
		
		for(int i = 0; i < englishBandNames.size(); i++){
			Answer wrong = new Answer();
			wrong.setTextDE(replaceBrackets(germanBandNames.get(i)));
			wrong.setTextEN(replaceBrackets(englishBandNames.get(i)));
			bandMemberQuestion.addWrongAnswer(wrong);
		}
		
		return bandMemberQuestion;
	}
	
	//bands under contract to sony recordlabel
	private static Question bandRecordLabel() throws Exception{
		if(!DBPediaService.isAvailable()){
			Logger.info("DBPedia isn't available!");
			throw new Exception();
		}
		Resource label = DBPediaService.loadStatements(DBPedia.createResource("Sony_Music_Entertainment"));
		
		SelectQueryBuilder musicQuery = DBPediaService.createQueryBuilder()
				.addWhereClause(RDF.type, DBPediaOWL.Band)
				.addWhereClause(DBPediaOWL.recordLabel, label)
				.addFilterClause(RDFS.label, Locale.GERMAN)
				.addFilterClause(RDFS.label, Locale.ENGLISH)
				.setLimit(4);
		
		Model sonyBands = DBPediaService.loadStatements(musicQuery.toQueryString());
		
		List<String> englishSonyBandNames =
				DBPediaService.getResourceNames(sonyBands, Locale.ENGLISH);
		List<String> germanSonyBandNames =
				DBPediaService.getResourceNames(sonyBands, Locale.GERMAN);
		
		musicQuery.removeWhereClause(DBPediaOWL.recordLabel, label);
		musicQuery.addMinusClause(DBPediaOWL.recordLabel, label);
		musicQuery.setLimit(5-englishSonyBandNames.size());
		
		Model noSonyLabel = DBPediaService.loadStatements(musicQuery.toQueryString());
		
		List<String> englishNoSonyBandNames = DBPediaService.getResourceNames(noSonyLabel, Locale.ENGLISH);
		List<String> germanNoSonyBandNames = DBPediaService.getResourceNames(noSonyLabel, Locale.GERMAN);
		
		Question sonyLabelQuestion = new Question();
		sonyLabelQuestion.setTextDE("Diese Bands sind bei Sony Music Entertainment unter Vertrag");
		sonyLabelQuestion.setTextEN("These bands are under contract to Sony Music Entertainment");
		
		
		for(int i = 0; i < englishSonyBandNames.size(); i++){
			Answer right = new Answer();
			right.setTextDE(replaceBrackets(germanSonyBandNames.get(i)));
			right.setTextEN(replaceBrackets(englishSonyBandNames.get(i)));
			sonyLabelQuestion.addRightAnswer(right);
		}
		
		for(int i = 0; i < englishNoSonyBandNames.size(); i++){
			Answer wrong = new Answer();
			wrong.setTextDE(replaceBrackets(germanNoSonyBandNames.get(i)));
			wrong.setTextEN(replaceBrackets(englishNoSonyBandNames.get(i)));
			sonyLabelQuestion.addWrongAnswer(wrong);
		}
		
		return sonyLabelQuestion;
	}
	
	//austrian bands under contract to sony record label
	private static Question austrianBandAndSonyLabel() throws Exception{
		if(!DBPediaService.isAvailable()){
			Logger.info("DBPedia isn't available!");
			throw new Exception();
		}
		Resource label = DBPediaService.loadStatements(DBPedia.createResource("Sony_Music_Entertainment"));
		Resource austria = DBPediaService.loadStatements(DBPedia.createResource("Austria"));
		
		SelectQueryBuilder musicQuery = DBPediaService.createQueryBuilder()
				.addWhereClause(RDF.type, DBPediaOWL.Band)
				.addWhereClause(DBPediaOWL.hometown, austria)
				.addWhereClause(DBPediaOWL.recordLabel, label)
				.addFilterClause(RDFS.label, Locale.GERMAN)
				.addFilterClause(RDFS.label, Locale.ENGLISH);
		
		Model atSonyBands = DBPediaService.loadStatements(musicQuery.toQueryString());
		
		List<String> englishATSonyBandNames =
				DBPediaService.getResourceNames(atSonyBands, Locale.ENGLISH);
		List<String> germanATSonyBandNames =
				DBPediaService.getResourceNames(atSonyBands, Locale.GERMAN);
		
		
		musicQuery.removeWhereClause(DBPediaOWL.recordLabel, label);
		musicQuery.addMinusClause(DBPediaOWL.recordLabel, label);
		musicQuery.setLimit(5-englishATSonyBandNames.size());
		
		Model atBand = DBPediaService.loadStatements(musicQuery.toQueryString());
		
		List<String> englishATBandNames = DBPediaService.getResourceNames(atBand, Locale.ENGLISH);
		List<String> germanATBandNames = DBPediaService.getResourceNames(atBand, Locale.GERMAN);
		
		Question atSonyLabelQuestion = new Question();
		atSonyLabelQuestion.setTextDE("Diese österreichischen Bands sind bei Sony Music Entertainment unter Vertrag");
		atSonyLabelQuestion.setTextEN("These austrian bands are under contract to Sony Music Entertainment");
		
		for(int i = 0; i < englishATSonyBandNames.size(); i++){
			Answer right = new Answer();
			right.setTextDE(replaceBrackets(germanATSonyBandNames.get(i)));
			right.setTextEN(replaceBrackets(englishATSonyBandNames.get(i)));
			atSonyLabelQuestion.addRightAnswer(right);
		}
		
		for(int i = 0; i < englishATBandNames.size(); i++){
			Answer wrong = new Answer();
			wrong.setTextDE(replaceBrackets(germanATBandNames.get(i)));
			wrong.setTextEN(replaceBrackets(englishATBandNames.get(i)));
			atSonyLabelQuestion.addWrongAnswer(wrong);
		}
		
		return atSonyLabelQuestion;
	}
	
	/**
	 * Helper method to remove brackets after name
	 */
	private static String replaceBrackets(String text){
		
		if(text.contains("(")&&text.contains(")")){
			return text.substring(0, text.indexOf("("));
		}
		return text;
		
	}
}
