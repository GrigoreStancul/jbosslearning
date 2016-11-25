package com.grisha.books.controller;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Model;
import javax.enterprise.inject.Produces;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

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

	@Produces
	@Named
	private Book newBook;

	@PostConstruct
	public void init() {
		newBook = new Book();
	}

	public void addNewBook() throws Exception {
		try {
			log.info("Creating book " + newBook.getTitle());
			bookDao.persist(newBook);
			final FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_INFO, "Done!", "Book Added");
			facesContext.addMessage(null, m);
		} catch (Exception e) {
			final String errorMessage = ExceptionsUtils.getRootErrorMessage(e);
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, "Error while saving data");
			facesContext.addMessage(null, m);
			log.log(Level.WARNING, "exception in addNewBook", e);
		}
	}
}
