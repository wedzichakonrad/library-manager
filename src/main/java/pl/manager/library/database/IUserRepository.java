package pl.manager.library.database;

import pl.manager.library.model.User;

import java.util.List;

public interface IUserRepository {
    User getUserByLogin(String login);
    List<User> getAllUsers();
}