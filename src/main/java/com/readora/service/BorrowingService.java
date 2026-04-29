package com.readora.service;

import com.readora.database.BookDAO;
import com.readora.database.BorrowRecordDAO;
import com.readora.model.Book;
import com.readora.model.BorrowRecord;
import com.readora.user.UserAccount;
import com.readora.model.BorrowStatus;

import java.time.LocalDate;
import java.util.List;

public final class BorrowingService {

    private static final BookDAO bookDAO = new BookDAO();
    private static final BorrowRecordDAO borrowRecordDAO = new BorrowRecordDAO();

    private BorrowingService() {}

    public static boolean borrowBook(UserAccount student, Book book) {
        if (student == null || book == null) {
            return false;
        }

        if (!"Available".equalsIgnoreCase(book.getStatus())) {
            return false;
        }

        BorrowRecord record = new BorrowRecord(
                generateRecordId(),
                student.getStudentId(),
                student.getFullName(),
                book.getBookId(),
                book.getTitle(),
                LocalDate.now(),
                LocalDate.now().plusDays(7),
                null,
                BorrowStatus.BORROWED
        );

        book.setStatus("Borrowed");

        boolean recordSaved = borrowRecordDAO.insert(record);
        boolean bookUpdated = bookDAO.update(book);

        AppState.refreshAll();

        return recordSaved && bookUpdated;
    }

    public static boolean returnBook(BorrowRecord record) {
        if (record == null || "RETURNED".equalsIgnoreCase(record.getStatus())) {
            return false;
        }

        record.setReturnDate(LocalDate.now());
        record.setBorrowStatus(BorrowStatus.RETURNED);

        Book book = bookDAO.findById(record.getBookId());

        if (book != null) {
            book.setStatus("Available");
            bookDAO.update(book);
        }

        boolean success = borrowRecordDAO.update(record);
        AppState.refreshAll();

        return success;
    }

    public static void updateOverdueRecords() {
        List<BorrowRecord> records = borrowRecordDAO.findActiveBorrowedRecords();

        for (BorrowRecord record : records) {
            if (record.isOverdue()) {
                record.setBorrowStatus(BorrowStatus.OVERDUE);
                borrowRecordDAO.update(record);
            }
        }

        AppState.refreshBorrowRecords();
    }

    private static String generateRecordId() {
        int nextNumber = borrowRecordDAO.findAll().size() + 1;
        String id = String.format("R%03d", nextNumber);

        while (borrowRecordDAO.findById(id) != null) {
            nextNumber++;
            id = String.format("R%03d", nextNumber);
        }

        return id;
    }
}