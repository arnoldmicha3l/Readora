package com.readora.service;

import com.readora.database.BookDAO;
import com.readora.model.Book;
import com.readora.user.ValidationService;

public final class BookService {

    private static final BookDAO bookDAO = new BookDAO();

    private BookService() {}

    public static boolean addBook(Book book) {
        if (book == null
                || ValidationService.isBlank(book.getBookId())
                || ValidationService.isBlank(book.getTitle())
                || ValidationService.isBlank(book.getAuthor())
                || ValidationService.isBlank(book.getCategory())
                || ValidationService.isBlank(book.getStatus())) {
            return false;
        }

        if (bookDAO.findById(book.getBookId()) != null) {
            return false;
        }

        boolean success = bookDAO.insert(book);
        AppState.refreshBooks();
        return success;
    }

    public static boolean updateBook(Book book) {
        if (book == null
                || ValidationService.isBlank(book.getBookId())
                || ValidationService.isBlank(book.getTitle())
                || ValidationService.isBlank(book.getAuthor())
                || ValidationService.isBlank(book.getCategory())
                || ValidationService.isBlank(book.getStatus())) {
            return false;
        }

        boolean success = bookDAO.update(book);
        AppState.refreshBooks();
        return success;
    }

    public static boolean deleteBook(String bookId) {
        if (ValidationService.isBlank(bookId)) {
            return false;
        }

        boolean success = bookDAO.delete(bookId);
        AppState.refreshBooks();
        return success;
    }
}