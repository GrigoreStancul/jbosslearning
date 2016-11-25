package com.grisha.books.controller;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.enterprise.inject.Model;
import javax.enterprise.inject.Produces;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.UserTransaction;

import com.grisha.books.control.BookDao;
import com.grisha.books.entity.Book;
import com.grisha.books.util.exceptions.ExceptionsUtils;

@Model
public class BooksManagementService {

	@Inject
	private Logger log;

	@Inject
	private FacesContext facesContext;

	@Inject
	private BookDao bookDao;

	@Resource
	private UserTransaction userTransaction;

	@Produces
	@Named
	private Book newBook;

	@Produces
	@Named
	private String searchBox;

	@PostConstruct
	public void init() {
		newBook = new Book();
		searchBox = ".*";
	}

	public void addNewBook() throws Exception {
		try {
			log.info("Creating book " + newBook.getTitle());
			userTransaction.begin();
			bookDao.persist(newBook);
			userTransaction.commit();
			final FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_INFO, "Done!", "Book Added");
			facesContext.addMessage(null, m);
		} catch (Exception e) {
			userTransaction.rollback();
			final String errorMessage = ExceptionsUtils.getRootErrorMessage(e);
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, "Error while saving data");
			facesContext.addMessage(null, m);
			log.log(Level.WARNING, "exception in addNewBook", e);
		}
	}

	public void searchBooks() {
		List<Book> books = bookDao.findAll();
		books.stream().filter(b -> b.getTitle().matches(searchBox)).forEach(b -> log.info(b.toString()));
	}

}
