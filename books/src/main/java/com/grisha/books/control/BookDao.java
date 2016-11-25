package com.grisha.books.control;

import com.grisha.books.entity.Book;

public class BookDao extends AbstractDao<Book> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BookDao() {
		super(Book.class);
	}


}
