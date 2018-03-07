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
	/*
	@Test
	public void getBooksByNameParamTest(){
		LOG.info("getBookByIdTest started: Gets book by name from the service and checks it's value");
		ArrayList<Book> resultList = gson.fromJson(client.performGET(NEWSPAPER_URL +"/params?name=Inferno"), ArrayList.class);
		Reporter.log("Response is null: "+(resultList == null));
		assertNotNull(resultList);
		Reporter.log("Verification if equal. Actual=["+resultList.size()+"] expected["+1+"] is = "+(resultList.size() == 1));
		assertEquals(resultList.size(),1);
	}
	
	@Test
	public void getBooksByAuthorParamTest(){
		LOG.info("getBooksByAuthorParamTest started: Gets book by author from the service and checks it's size");
		ArrayList<Book> resultList = gson.fromJson(client.performGET(NEWSPAPER_URL +"/params?author=Scott%20Mariani"), ArrayList.class);
		Reporter.log("Response is null: "+(resultList == null));
		assertNotNull(resultList);
		Reporter.log("Verification if equal. Actual=["+resultList.size()+"] expected["+2+"] is = "+(resultList.size() == 2));
		assertEquals(resultList.size(),2);
	}
	
	@Test
	public void getBooksByNameAndAuthorParamTest(){
		LOG.info("getBooksByNameAndAuthorParamTest started: Gets book with author and name params from the service and checks it's size");
		ArrayList<Book> resultList = gson.fromJson(client.performGET(NEWSPAPER_URL +"/params?name=Inferno&author=Dan%20Brown"),
				ArrayList.class);
		Reporter.log("Response is null: "+(resultList == null));
		assertNotNull(resultList);
		Reporter.log("Verification if equal. Actual=["+resultList.size()+"] expected["+1+"] is = "+(resultList.size() == 1));
		assertEquals(resultList.size(),1);
	}
	
	@Test
	public void postBookWithExistId() throws NotValidNewsException {
		LOG.info("postBookWithExistId started: update book with aleready exist id in the service");
		Book replaceBook = new Book("Some Name", "Some Author", "Some Genre",103);
		client.doPost(NEWSPAPER_URL +"/5", replaceBook);
		Book newBook = gson.fromJson(client.performGET(NEWSPAPER_URL +"/5"), Book.class);
		Reporter.log("Verification if equal. Actual=["+newBook+"] expected["+replaceBook+"] is = "+(newBook.equals(replaceBook)));
		assertEquals(newBook,replaceBook);
	}
	
	@Test
	public void postBookWithNotExistId() throws NotValidNewsException {
		LOG.info("postBookWithNotExistId started: add book without aleready exist id in the service");
		Book newBook = new Book("New Book", "Author", "Genre",102);
		
		client.doPost(NEWSPAPER_URL +"/102", newBook);
		
		Book actualBook = gson.fromJson(client.performGET(NEWSPAPER_URL +"/102"), Book.class);
		Reporter.log("Response is null: "+(actualBook == null));
		assertNotNull(actualBook);
		Reporter.log("Verification if equal. Actual=["+actualBook+"] expected["+newBook+"] is = "+(actualBook.equals(newBook)));
		assertEquals(actualBook,newBook);
		Reporter.log("Deleting created book");
		client.doDelete(NEWSPAPER_URL +"/102");
	}
	
	@Test
	public void deleteBookWithExistId() throws NotValidNewsException {
		LOG.info("deleteBookWithExistId started: delete book with aleready exist id in the service");
		Book book = new Book("The Motorcycle Diaries", "Ernesto 'Che' Guevara", "Travel",9);
		String actualResponse = client.doDelete(NEWSPAPER_URL +"/9");
		String expextedResponse = "{\"Message\":\"Book was deleted successfully\"}";
		
		Reporter.log("Response is null: "+(actualResponse == null));
		assertNotNull(actualResponse);
		Reporter.log("Verification if equal. Actual=["+actualResponse+"] expected["+expextedResponse+"] is = "
				+(actualResponse.equals(expextedResponse)));
		assertEquals(actualResponse,expextedResponse);
		
		Reporter.log("Adding deleted book");
		client.doPost(NEWSPAPER_URL +"/9", book);
	}
	
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
