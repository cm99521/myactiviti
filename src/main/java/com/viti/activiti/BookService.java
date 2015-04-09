<<<<<<< HEAD
package com.viti.activiti;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


public class BookService {

	@PersistenceContext
	private EntityManager entityManager;
	
	public Book createBook(List<String> authorList){
		Book book = new Book();
		for(String author : authorList){
			book.getAuthors().add(author);
		}
		
		entityManager.persist(book);
		System.out.println("leaving createbook@@@@@@@@@@@@@@@@@@@@@@@@@@@");
		return book;
	}
}
=======
package com.viti.activiti;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


public class BookService {

	@PersistenceContext
	private EntityManager entityManager;
	
	public Book createBook(List<String> authorList){
		Book book = new Book();
		for(String author : authorList){
			book.getAuthors().add(author);
		}
		
		entityManager.persist(book);
		System.out.println("leaving createbook@@@@@@@@@@@@@@@@@@@@@@@@@@@");
		return book;
	}
}
>>>>>>> refs/remotes/origin/master
