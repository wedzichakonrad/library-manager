package pl.manager.library.core;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.manager.library.authentication.IAuthenticator;
import pl.manager.library.database.IBookRepository;
import pl.manager.library.gui.IGUI;
import pl.manager.library.model.Book;
import pl.manager.library.model.Role;
import pl.manager.library.model.User;

@Component
@RequiredArgsConstructor
public class Core implements ICore {
    private final IGUI gui;
    private final IAuthenticator authenticator;
    private final IBookRepository bookRepository;

    @Override
    public void run() {
        User user = gui.readUserData();
        User authenticatedUser = authenticator.authenticate(user);

        if (authenticatedUser == null) {
            gui.showMessage("Authentication failed. Exiting...");
            return;
        }

        handleSession(authenticatedUser);
    }

    private void handleSession(User user) {
        while (true) {
            gui.showMenuForRole(user.getRole());
            String choice = gui.readUserChoice();

            if (!gui.isUserChoiceValid(choice, user.getRole())) {
                gui.showMessage("Choice does not exist. Please try again.");
                continue;
            }

            if (isExitRequested(choice, user.getRole())) {
                gui.showMessage("Exiting...");
                break;
            }

            handleMenuChoice(choice);
        }
    }

    private boolean isExitRequested(String choice, Role role) {
        return (choice.equals("4") && role == Role.USER) || (choice.equals("7") && role == Role.ADMIN);
    }

    private void handleMenuChoice(String choice) {
        switch (choice) {
            case "1" -> viewBooks();
            case "2" -> findByAuthor();
            case "3" -> findByTitle();
            case "4" -> addBook();
            case "5" -> removeBook();
            case "6" -> editBook();
        }
    }

    private void viewBooks() {
        gui.showBooks(bookRepository.getAllBooks());
        gui.showMessage("---------------------------");
    }

    private void findByAuthor() {
        gui.showMessage("Enter book author: ");
        gui.showBooks(bookRepository.findByAuthor(gui.readUserChoice()));
    }

    private void findByTitle() {
        gui.showMessage("Enter book title: ");
        gui.showBooks(bookRepository.findByTitle(gui.readUserChoice()));
    }

    private void addBook() {
        Book newBook = gui.readBookData();
        if (newBook != null) {
            bookRepository.addBook(newBook.getAuthor(), newBook.getTitle());
            gui.showMessage("Book added.");
        }
    }

    private void removeBook() {
        Integer id = gui.readBookId("Enter book ID to remove: ");
        if (id != null) {
            boolean deleted = bookRepository.deleteBook(id);
            gui.showMessage(deleted ? "Book removed." : "Book with given ID not found.");
        }
    }

    private void editBook() {
        Integer id = gui.readBookId("Enter book ID to edit: ");
        if (id == null) return;

        Book bookToEdit = bookRepository.getBookById(id);
        if (bookToEdit == null) {
            gui.showMessage("Book with given ID not found.");
            return;
        }

        Book updatedData = gui.readBookData();
        if (updatedData != null) {
            bookToEdit.setAuthor(updatedData.getAuthor());
            bookToEdit.setTitle(updatedData.getTitle());
            boolean updated = bookRepository.updateBook(bookToEdit);
            gui.showMessage(updated ? "Book updated successfully." : "Failed to update the book.");
        }
    }
}