package com.sample.springbootelasticsearch.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "post", type = "post",shards = 1)
public class Post {
	@Id
	String id;
	String title;
	String author;
	String content;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	
	
	public Post() {
		
	}

	public Post(String id, String title, String author, String content) {
		this.id = id;
		this.title = title;
		this.author = author;
		this.content = content;
	}

	public String toString() {
		 return "Post{" +
	                "id='" + id + '\'' +
	                ", title='" + title + '\'' +
	                ", author='" + author + '\'' +
	                ", content='" + content + '\'' +
	                '}';
		
	}

	
}
