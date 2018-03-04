package com.epam.rs.client;

import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.testng.Reporter;

import com.epam.model.Book;
import com.epam.model.NotValidNewsException;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class ClientHandler {
    private static final Logger LOG = Logger.getLogger(ClientHandler.class);
    private Client client;
    private WebResource webResource;
    private ClientResponse response;

    public ClientHandler() {
        client = Client.create();
    }

    public String performGET(String url) {
        LOG.info(String.format(" ---- %s.perform GET() ----- ", this.getClass().getSimpleName()));
        webResource = client.resource(url);
        LOG.info("Setting accept 'application/json'");
        response = webResource.accept("application/json").get(ClientResponse.class);
        LOG.info("Response: " + response);
        return response.getEntity(String.class);
    }

    public String doPost(String url, Book book) throws NotValidNewsException {
        Reporter.log("doPost method started");
        if (!isValid(book)) {
            Reporter.log("Book is NOT VALID: " + book);
            throw new NotValidNewsException();
        }
        webResource = client.resource(url);
        Reporter.log("Setting accept 'application/json'");
        response = webResource.accept("application/json")
                .type(MediaType.APPLICATION_FORM_URLENCODED_TYPE)
                .post(ClientResponse.class, transformToUrlUncodedType(book));
        Reporter.log("Response: " + response);
        return response.getEntity(String.class);
    }

    public String doDelete(String url) {
        Reporter.log("doDelete method started");
        webResource = client.resource(url);
        Reporter.log("Setting accept 'application/json'");
        response = webResource.accept("application/json")
                .delete(ClientResponse.class);
        Reporter.log("Response: " + response);
        return response.getEntity(String.class);
    }

    private String transformToUrlUncodedType(Book book) {
        return "name=" + book.getName() + "&" +
                "author=" + book.getAuthor() + "&" +
                "genre=" + book.getGenre() + "&" +
                "id=" + book.getId();
    }

    private boolean isValid(Book book) {
        boolean isValid;
        if (book.getId() > 0 && book.getAuthor() != null && book.getName() != null && book.getGenre() != null) {
            isValid = true;
        } else {
            isValid = false;
        }
        return isValid;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public WebResource getWebResource() {
        return webResource;
    }

    public void setWebResource(WebResource webResource) {
        this.webResource = webResource;
    }

    public ClientResponse getResponse() {
        return response;
    }

    public void setResponse(ClientResponse response) {
        this.response = response;
    }


}
