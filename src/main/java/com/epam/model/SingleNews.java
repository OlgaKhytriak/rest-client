package com.epam.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "news")
public class SingleNews {
    private Integer id;
    private String title;
    private String category;
    private String description;
    private String link;

    public SingleNews(Integer id, String title, String category, String description, String link) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.description = description;
        this.link = link;
    }

    public SingleNews() {
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((title == null) ? 0 : title.hashCode());
        return result;
    }


    @Override
    public boolean equals(Object obj) {
        SingleNews news = (SingleNews) obj;
        if (this.id == news.id || this.title.equals(news.title)) {
            return true;
        }
        return false;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
