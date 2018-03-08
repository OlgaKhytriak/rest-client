package com.epam.tests;

import java.util.ArrayList;

import static org.testng.Assert.*;

import com.epam.model.SingleNews;
import org.apache.log4j.Logger;
import org.testng.Reporter;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.epam.model.NotValidNewsException;
import com.epam.rs.client.ClientHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class NewsPaperTest {
	private static final Logger LOG = Logger.getLogger(NewsPaperTest.class);
	private  static final String NEWSPAPER_URL = "http://localhost:8080/rest/service/news";

	private  final ClientHandler clientHandler;
	private  final Gson gson;

	public NewsPaperTest() {
		clientHandler = new ClientHandler();
		gson = new GsonBuilder().setPrettyPrinting().create();
	}

	@Test
	public void getAllNewsTest(){
		LOG.info(String.format(" ---- %s. getAllNewsTest() ----- ", this.getClass().getSimpleName()));
		ArrayList<SingleNews> resultList = gson.fromJson(clientHandler.performGET(NEWSPAPER_URL), ArrayList.class);
		LOG.info("Is response null? -  "+(resultList == null));
		assertNotNull(resultList);
	}

	@Test
	public void getNewsByIdTest(){
		LOG.info(String.format(" ---- %s. getNewsByIdTest() ----- ", this.getClass().getSimpleName()));
		SingleNews expectedNews = new SingleNews(1,"Gold-medal","Sport","Gold medalist Abramenko carries Ukrainian flag at Winter Olympics 2018 closing ceremony","https://24tv.ua");
		SingleNews actualNews = gson.fromJson(clientHandler.performGET(NEWSPAPER_URL +"/1"), SingleNews.class);
		LOG.info("Response is null: "+(actualNews == null));
		assertNotNull(actualNews);
		LOG.info(String.format("Actual result = %s",actualNews.toString()));
		LOG.info(String.format("Expect result = %s",expectedNews.toString()));
		assertEquals(actualNews,expectedNews,"Is not equals");
	}

	@Test
	public void getNewsByTitleTest(){
		LOG.info(String.format(" ---- %s. getNewsByTitleTest() ----- ", this.getClass().getSimpleName()));
		ArrayList<SingleNews> resultList = gson.fromJson(clientHandler.performGET(NEWSPAPER_URL +"/params?title=Olympics"), ArrayList.class);
		LOG.info("Response is null: "+(resultList == null));
		assertNotNull(resultList);
		LOG.info("Verification if equal. Actual=["+resultList.size()+"] expected["+1+"] is = "+(resultList.size() == 1));
		assertEquals(resultList.size(),1);
	}

	@Test
	public void getNewsByCategoryTest(){
		LOG.info(String.format(" ---- %s. getNewsByCategoryTest() ----- ", this.getClass().getSimpleName()));
		ArrayList<SingleNews> resultList = gson.fromJson(clientHandler.performGET(NEWSPAPER_URL +"/params?category=Sport"), ArrayList.class);
		LOG.info("Response is null: "+(resultList == null));
		assertNotNull(resultList);
		LOG.info("Verification if equal. Actual=["+resultList.size()+"] expected["+4+"] is = "+(resultList.size() == 4));
		assertEquals(resultList.size(),4);
	}

	@Test
	public void getNewsByTitleAndCategoryTest(){
		LOG.info(String.format(" ---- %s. getNewsByTitleAndCategoryTest() ----- ", this.getClass().getSimpleName()));
		ArrayList<SingleNews> resultList = gson.fromJson(clientHandler.performGET(NEWSPAPER_URL +"/params?title=Shakhtar&category=Sport"),
				ArrayList.class);
		LOG.info("Response is null: "+(resultList == null));
		assertNotNull(resultList);
		LOG.info("Verification if equal. Actual=["+resultList.size()+"] expected["+1+"] is = "+(resultList.size() == 1));
		assertEquals(resultList.size(),1);
	}


	@Test
	public void postBookWithNotExistId() throws NotValidNewsException {//---
		LOG.info("postBookWithNotExistId started: add book without aleready exist id in the service");
		SingleNews newNews = new SingleNews(102,"New-title","Newe-category","Bla-bla-bla","https://ukr.net");

		clientHandler.performPost(NEWSPAPER_URL +"/102", newNews);
		/*
		Book actualBook = gson.fromJson(client.performGET(NEWSPAPER_URL +"/102"), Book.class);
		Reporter.log("Response is null: "+(actualBook == null));
		assertNotNull(actualBook);
		Reporter.log("Verification if equal. Actual=["+actualBook+"] expected["+newBook+"] is = "+(actualBook.equals(newBook)));
		assertEquals(actualBook,newBook);
		Reporter.log("Deleting created book");
		client.doDelete(NEWSPAPER_URL +"/102");
		*/
	}

	@Test
	public void postUpdateNewsWithExistIdTest() throws NotValidNewsException {//-----
		LOG.info(String.format(" ---- %s. postUpdateNewsWithExistIdTest() ----- ", this.getClass().getSimpleName()));
		SingleNews replaceNews = new SingleNews(101,"New-new-new","Cat-new","Bla bla bla","https://ukr.net");
		clientHandler.performPost(NEWSPAPER_URL+"/5", replaceNews);
		SingleNews newNews = gson.fromJson(clientHandler.performGET(NEWSPAPER_URL+"/5"), SingleNews.class);
		LOG.info("Verification if equal. Actual=["+newNews+"] expected["+replaceNews+"] is = "+(newNews.equals(replaceNews)));
		assertEquals(newNews,replaceNews);
	}

	@Test
	public void deleteBookWithExistId() throws NotValidNewsException {
		LOG.info("deleteBookWithExistId started: delete book with aleready exist id in the service");
		//SingleNews singleNews = new SingleNews("The Motorcycle Diaries", "Ernesto 'Che' Guevara", "Travel",9);
		String actualResponse = clientHandler.doDelete(NEWSPAPER_URL +"/9");
		String expextedResponse = "{\"Message\":\"News is deleted successfully\"}";
		
		Reporter.log("Response is null: "+(actualResponse == null));
		assertNotNull(actualResponse);
		Reporter.log("Verification if equal. Actual=["+actualResponse+"] expected["+expextedResponse+"] is = "
				+(actualResponse.equals(expextedResponse)));
		assertEquals(actualResponse,expextedResponse);
		
		//Reporter.log("Adding deleted book");
		//client.doPost(NEWSPAPER_URL +"/9", book);
	}
/*
	@Test
	public void deleteBookWithNotExistId() throws NotValidNewsException {
		LOG.info("deleteBookWithNotExistId started: delete book without aleready exist id in the service");
		String actualResponse = client.doDelete(NEWSPAPER_URL +"/1111").replaceAll("\\s","");
		String expextedResponse = "{\"Message\":\"Bookwasnotfound\",\"Title\":\"Failure\",\"Code\":\"204-NoContent\"}";
		Reporter.log("Response is null: "+(actualResponse == null));
		assertNotNull(actualResponse);
		Reporter.log("Verification if equal. Actual=["+actualResponse+"] expected["+expextedResponse+"] is = "
				+(actualResponse.equals(expextedResponse)));
		assertEquals(actualResponse,expextedResponse);
	}
	
	@Test
	public void getBooksByWithoutParamTest(){
		LOG.info("getBooksByNameAndAuthorParamTest started: Gets book without author and name params from the service and checks it's size");
		String actualResponse = client.performGET(NEWSPAPER_URL +"/params?").replaceAll("\\s","");
		String expextedResponse = "{\"Message\":\"Therearenotinputparameters:[name:null,author:null]\""
				+ ",\"Title\":\"Failure\",\"Code\":\"400-BadRequest\"}";
		Reporter.log("Response is null: "+(actualResponse == null));
		assertNotNull(actualResponse);
		Reporter.log("Verification if equal. Actual=["+actualResponse+"] expected["+expextedResponse+"] is = "
				+(actualResponse.equals(expextedResponse)));
		assertEquals(actualResponse,expextedResponse);
	}
*/
}
