package pl.manager.library.database;

import pl.manager.library.model.User;

public interface IUserRepository {
    User getUserByLogin(String login);
}