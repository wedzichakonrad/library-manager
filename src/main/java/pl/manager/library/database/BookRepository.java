package pl.manager.library.database;

import org.springframework.stereotype.Component;
import pl.manager.library.model.Book;

import java.util.ArrayList;
import java.util.List;

@Component
public class BookRepository implements IBookRepository {
    private final List<Book> books = new ArrayList<Book>();
    private int bookId = 0;

    public BookRepository() {
        books.add(new Book(this.bookId++, "Lalka", "Bolesław Prus"));
        books.add(new Book(this.bookId++, "Rok 1984", "George Orwell"));
        books.add(new Book(this.bookId++, "Zbrodnia i Kara", "Fiodor Dostojewski"));
        books.add(new Book(this.bookId++, "Wiedzmin", "Andrzej Sapkowski"));
    }

    @Override
    public List<Book> getAllBooks() {
        return books;
    }

    @Override
    public Book getBookById(int id) {
        for (Book book : books) {
            if (book.getId() == id) {
                return book;
            }
        }
        return null;
    }

    @Override
    public List<Book> findByAuthor(String author) {
        List<Book> result = new ArrayList<Book>();
        for (Book book : books) {
            if (book.getAuthor().equalsIgnoreCase(author)) {
                result.add(book);
            }
        }
        return result;
    }

    @Override
    public List<Book> findByTitle(String title) {
        List<Book> result = new ArrayList<Book>();
        for (Book book : books) {
            if (book.getTitle().equalsIgnoreCase(title)) {
                result.add(book);
            }
        }
        return result;
    }

    @Override
    public void addBook(String author, String title) {
        books.add(new Book(this.bookId++, title, author));
    }

    @Override
    public boolean deleteBook(int id) {
        Book bookToRemove = getBookById(id);
        if (bookToRemove != null) {
            books.remove(bookToRemove);
            return true;
        }
        return false;
    }

    @Override
    public boolean updateBook(Book book) {
        Book existingBook = getBookById(book.getId());
        if (existingBook != null) {
            existingBook.setTitle(book.getTitle());
            existingBook.setAuthor(book.getAuthor());
            return true;
        }
        return false;
    }
}