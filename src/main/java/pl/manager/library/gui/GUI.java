package pl.manager.library.gui;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.manager.library.model.Book;
import pl.manager.library.model.Role;
import pl.manager.library.model.User;

import java.util.List;
import java.util.Scanner;

@Component
@RequiredArgsConstructor
public class GUI implements IGUI {
    private final Scanner scanner;

    private void showUserMenu() {
        System.out.println("1. View Books");
        System.out.println("2. Search by author");
        System.out.println("3. Search by title");
        System.out.println("4. Exit");
    }

    private void showAdminMenu() {
        System.out.println("1. View Books");
        System.out.println("2. Search by author");
        System.out.println("3. Search by title");
        System.out.println("4. Add Book");
        System.out.println("5. Remove Book");
        System.out.println("6. Edit Book");
        System.out.println("7. Exit");
    }

    @Override
    public User readUserData() {
        System.out.print("Enter user name: ");
        String name = scanner.nextLine();
        System.out.print("Enter user password: ");
        String password = scanner.nextLine();
        return new User(name, password);
    }

    @Override
    public void showMenuForRole(Role role) {
        if (role == Role.ADMIN) {
            this.showAdminMenu();
        } else {
            this.showUserMenu();
        }
    }

    @Override
    public String readUserChoice() {
        return scanner.nextLine();
    }

    @Override
    public boolean isUserChoiceValid(String choice, Role role) {
        try {
            int numChoice = Integer.parseInt(choice);
            if (role == Role.USER) {
                return numChoice >= 1 && numChoice <= 4;
            } else {
                return numChoice >= 1 && numChoice <= 7;
            }
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void showBooks(List<Book> books) {
        if (books.isEmpty()) {
            System.out.println("No books found.");
        } else {
            for (Book book : books) {
                System.out.println(book.getId() + " " + book.getAuthor() + " - " + book.getTitle());
            }
        }
    }

    @Override
    public Integer readBookId(String prompt) {
        System.out.print(prompt);
        String input = scanner.nextLine();
        try {
            int id = Integer.parseInt(input);
            if (id < 0) {
                System.out.println("Book ID cannot be negative.");
                return null;
            }
            return id;
        } catch (NumberFormatException e) {
            System.out.println("Invalid book ID. Please enter a valid number.");
            return null;
        }
    }

    @Override
    public Book readBookData() {
        System.out.print("Enter book author: ");
        String author = scanner.nextLine();
        System.out.print("Enter book title: ");
        String title = scanner.nextLine();

        if (author.isBlank() || title.isBlank()) {
            System.out.println("Author and title cannot be empty!");
            return null;
        }
        return new Book(title, author);
    }

    @Override
    public void showMessage(String message) {
        System.out.println(message);
    }
}