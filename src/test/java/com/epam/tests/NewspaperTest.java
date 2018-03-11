package com.epam.tests;

import java.util.ArrayList;

import static org.testng.Assert.*;

import com.epam.model.SingleNews;
import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import com.epam.model.NotValidNewsException;
import com.epam.client.ClientHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class NewspaperTest {
    private static final Logger LOG = Logger.getLogger(NewspaperTest.class);
    private static final String NEWSPAPER_URL = "http://localhost:8080/rest/service/news";

    private final ClientHandler clientHandler;
    private final Gson gson;

    public NewspaperTest() {
        clientHandler = new ClientHandler();
        gson = new GsonBuilder().setPrettyPrinting().create();
    }

    @Test
    public void getAllNewsTest() {
        LOG.info(String.format(" ---- %s. getAllNewsTest() ----- ", this.getClass().getSimpleName()));
        ArrayList<SingleNews> resultList = gson.fromJson(clientHandler.performGET(NEWSPAPER_URL), ArrayList.class);
        LOG.info("Is response null? -  " + (resultList == null));
        assertNotNull(resultList);
    }

    @Test
    public void getNewsByIdTest() {
        LOG.info(String.format(" ---- %s. getNewsByIdTest() ----- ", this.getClass().getSimpleName()));
        SingleNews expectedNews = new SingleNews(1, "Gold-medal", "Sport", "Gold medalist Abramenko carries Ukrainian flag at Winter Olympics 2018 closing ceremony", "https://24tv.ua");
        SingleNews actualNews = gson.fromJson(clientHandler.performGET(NEWSPAPER_URL + "/1"), SingleNews.class);
        LOG.info("Is response null? - " + (actualNews == null));
        assertNotNull(actualNews);
        LOG.info(String.format("Actual result = %s", actualNews.toString()));
        LOG.info(String.format("Expect result = %s", expectedNews.toString()));
        assertEquals(actualNews, expectedNews, "Is not equals");
    }

    @Test
    public void getNewsByTitleTest() {
        LOG.info(String.format(" ---- %s. getNewsByTitleTest() ----- ", this.getClass().getSimpleName()));
        ArrayList<SingleNews> resultList = gson.fromJson(clientHandler.performGET(NEWSPAPER_URL + "/params?title=Olympics"), ArrayList.class);
        LOG.info("Response is null: " + (resultList == null));
        assertNotNull(resultList);
        LOG.info("Actual=[" + resultList.size() + "] expected[" + 1 + "] is = " + (resultList.size() == 1));
        assertEquals(resultList.size(), 1);
    }

    @Test
    public void getNewsByCategoryTest() {
        LOG.info(String.format(" ---- %s. getNewsByCategoryTest() ----- ", this.getClass().getSimpleName()));
        ArrayList<SingleNews> resultList = gson.fromJson(clientHandler.performGET(NEWSPAPER_URL + "/params?category=Sport"), ArrayList.class);
        LOG.info("Response is null: " + (resultList == null));
        assertNotNull(resultList);
        LOG.info("Actual=[" + resultList.size() + "] expected[" + 4 + "] is = " + (resultList.size() == 4));
        assertEquals(resultList.size(), 4);
    }

    @Test
    public void getNewsByTitleAndCategoryTest() {
        LOG.info(String.format(" ---- %s. getNewsByTitleAndCategoryTest() ----- ", this.getClass().getSimpleName()));
        ArrayList<SingleNews> resultList = gson.fromJson(clientHandler.performGET(NEWSPAPER_URL + "/params?title=Shakhtar&category=Sport"),
                ArrayList.class);
        LOG.info("Response is null: " + (resultList == null));
        assertNotNull(resultList);
        LOG.info("Actual=[" + resultList.size() + "] expected[" + 1 + "] is = " + (resultList.size() == 1));
        assertEquals(resultList.size(), 1);
    }

    @Test
    public void getNewsNegativeTest() {
        LOG.info(String.format(" ---- %s. getNewsNegativeTest() ----- ", this.getClass().getSimpleName()));
        String actualResponse = clientHandler.performGET(NEWSPAPER_URL + "/params?");
        String expextedResponse = "{\n" +
                "  \"Message\": \"There are not input parameters:  title \\u003d null; category \\u003d null\",\n" +
                "  \"Code\": \"400 - Bad Request\",\n" +
                "  \"Title\": \"Failure\"\n" +
                "}";

        assertEquals(actualResponse, expextedResponse);
    }
    @Test
    public void postAddNewsTest() throws NotValidNewsException {
        LOG.info(String.format(" ---- %s. postNewsTest() ----- ", this.getClass().getSimpleName()));
        SingleNews newNews = new SingleNews(102, "New-title", "New-category", "Bla-bla-bla", "https://ukr.net");

        clientHandler.performPost(NEWSPAPER_URL + "/102", newNews);
        SingleNews actualNews = gson.fromJson(clientHandler.performGET(NEWSPAPER_URL + "/102"), SingleNews.class);

        LOG.info(String.format("Actual result = %s", actualNews.toString()));
        LOG.info(String.format("Expect result = %s", newNews.toString()));
        assertEquals(actualNews, newNews);

        LOG.info("Deleting created news");
        clientHandler.doDelete(NEWSPAPER_URL + "/102");
    }

    @Test
    public void postUpdateNewsTest() throws NotValidNewsException {//-----
        LOG.info(String.format(" ---- %s. postUpdateNewsTest() ----- ", this.getClass().getSimpleName()));
        SingleNews replaceNews = new SingleNews(101, "New-new-new", "Cat-new", "Bla bla bla", "https://ukr.net");
        clientHandler.performPost(NEWSPAPER_URL + "/5", replaceNews);
        SingleNews newNews = gson.fromJson(clientHandler.performGET(NEWSPAPER_URL + "/5"), SingleNews.class);

        LOG.info(String.format("Actual result = %s", replaceNews.toString()));
        LOG.info(String.format("Expect result = %s", newNews.toString()));
        assertEquals(newNews, replaceNews);
    }

    @Test
    public void deleteNewsTest() throws NotValidNewsException {
        LOG.info(String.format(" ---- %s. deleteNewsTest() ----- ", this.getClass().getSimpleName()));
        String actualResponse = clientHandler.doDelete(NEWSPAPER_URL + "/9");
        String expextedResponse = "{\"Message\":\"News is deleted successfully\"}";

        LOG.info(String.format("Actual result = %s", actualResponse.toString()));
        LOG.info(String.format("Expect result = %s", expextedResponse.toString()));
        assertEquals(actualResponse, expextedResponse);
    }

    @Test
    public void deleteNewsNegativeTest() {
        LOG.info(String.format(" ---- %s. deleteNewsNegativeTest() ----- ", this.getClass().getSimpleName()));
        String actualResponse = clientHandler.doDelete(NEWSPAPER_URL + "/1111");
        String expextedResponse = "{\n" +
                "  \"Message\": \"News is not found for  id \\u003d 1111\",\n" +
                "  \"Code\": \"204 - No Content\",\n" +
                "  \"Title\": \"Failure\"\n" +
                "}";
        LOG.info(String.format("Actual result = %s", actualResponse.toString()));
        LOG.info(String.format("Expect result = %s", expextedResponse.toString()));
        assertEquals(actualResponse, expextedResponse);
    }



}
