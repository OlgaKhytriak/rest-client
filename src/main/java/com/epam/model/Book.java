package com.epam.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "book")
public class Book {
	@Override
	public String toString() {
		return "Book [id=" + id + ", name=" + name + ", author=" + author + ", genre=" + genre + "]";
	}

	private	Integer id;
	private String name;
	private String author;
	private String genre;

	public Book(String name,String author,String genre,Integer id) {
		this.author = author;
		this.name = name;
		this.genre = genre;
		this.id = id;
	}
	
	public Book() {}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}



	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		Book book = (Book) obj;
		if(this.id == book.id || this.name.equals(book.name)){
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
	
	
	
	
	
	
	
}
