package com.epam.rs.client;

import javax.ws.rs.core.MediaType;

import com.epam.model.SingleNews;
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
        LOG.info(String.format(" ---- %s. perform GET() ----- ", this.getClass().getSimpleName()));
        webResource = client.resource(url);
        response = webResource.accept("application/json").get(ClientResponse.class);
        LOG.info("Response: " + response);
        return response.getEntity(String.class);
    }

    public String performPost(String url, SingleNews singleNews) throws NotValidNewsException {
        LOG.info(String.format(" ---- %s. perform POST() ----- ", this.getClass().getSimpleName()));
        if (!isValid(singleNews)) {
            LOG.info("News is NOT VALID: " + singleNews);
            throw new NotValidNewsException();
        }
        webResource = client.resource(url);
        LOG.info(url + transformToUrlUncodedType(singleNews));
        Reporter.log("Setting accept 'application/json'");
        response = webResource.accept("application/json")
                .type(MediaType.APPLICATION_FORM_URLENCODED_TYPE)
                .post(ClientResponse.class, transformToUrlUncodedType(singleNews));
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

    private String transformToUrlUncodedType(SingleNews singleNews) {
        return "title=" + singleNews.getTitle() + "&" +
                "category=" + singleNews.getCategory() + "&" +
                "description=" + singleNews.getDescription() + "&" +
                "link=" + singleNews.getLink() + "&" +
                "id=" + singleNews.getId();
    }

    private boolean isValid(SingleNews singleNews) {
        boolean isValid;
        if (singleNews.getId() > 0 && singleNews.getTitle() != null && singleNews.getCategory() != null && singleNews.getDescription() != null) {
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
