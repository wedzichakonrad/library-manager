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

        while (authenticatedUser != null) {
            this.gui.showMenuForRole(authenticatedUser.getRole());
            gui.showMessage("Enter your choice: ");
            String choice = gui.readUserChoice();

            if (gui.isUserChoiceValid(choice, authenticatedUser.getRole())) {
                if (choice.equals("4") && authenticatedUser.getRole() == Role.USER) {
                    gui.showMessage("Exiting...");
                    break;
                } else if (choice.equals("7") && authenticatedUser.getRole() == Role.ADMIN) {
                    gui.showMessage("Exiting...");
                    break;
                } else {

                    switch (choice) {
                        case "1":
                            this.gui.showBooks(
                                    this.bookRepository.getAllBooks()
                            );
                            break;
                        case "2":
                            gui.showMessage("Enter book author to search: ");
                            String author = this.gui.readUserChoice();
                            this.gui.showBooks(
                                    this.bookRepository.findByAuthor(author)
                            );
                            break;
                        case "3":
                            gui.showMessage("Enter book title to search: ");
                            String title = this.gui.readUserChoice();
                            this.gui.showBooks(
                                    this.bookRepository.findByTitle(title)
                            );
                            break;
                        case "4":
                            Book newBook = this.gui.readBookData();
                            if (newBook == null) {
                                break;
                            }
                            this.bookRepository.addBook(newBook.getAuthor(), newBook.getTitle());
                            gui.showMessage("Book added successfully.");
                            break;
                        case "5":
                            Integer bookIdToRemove = this.gui.readBookId("Enter book ID to remove: ");
                            if (bookIdToRemove == null) {
                                break;
                            }
                            boolean isDeleted = this.bookRepository.deleteBook(bookIdToRemove);
                            if (isDeleted) {
                                gui.showMessage("Book removed successfully.");
                            } else {
                                gui.showMessage("Book with given ID not found.");
                            }
                            break;
                        case "6":
                            Integer bookIdToEdit = this.gui.readBookId("Enter book ID to edit: ");
                            if (bookIdToEdit == null) {
                                break;
                            }
                            Book bookToEdit = this.bookRepository.getBookById(bookIdToEdit);
                            if (bookToEdit == null) {
                                gui.showMessage("Book with given ID not found.");
                                break;
                            }
                            Book updatedData = this.gui.readBookData();
                            if (updatedData == null) {
                                break;
                            }
                            bookToEdit.setAuthor(updatedData.getAuthor());
                            bookToEdit.setTitle(updatedData.getTitle());
                            boolean isUpdated = this.bookRepository.updateBook(bookToEdit);
                            if (isUpdated) {
                                gui.showMessage("Book updated successfully.");
                            } else {
                                gui.showMessage("Failed to update the book.");
                            }
                            break;
                    }
                }
            } else {
                gui.showMessage("Invalid choice. Please try again.");
            }
        }

        if (authenticatedUser == null) {
            gui.showMessage("Authentication failed. Exiting...");
        }
    }
}