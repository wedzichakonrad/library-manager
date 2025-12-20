package pl.manager.library.database;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Component;
import pl.manager.library.model.Role;
import pl.manager.library.model.User;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserRepository implements IUserRepository {
    private final List<User> users = new ArrayList<>();

    public UserRepository() {
        this.addUser("admin", "admin", Role.ADMIN);
        this.addUser("konrad", "password", Role.USER);
    }

    private void addUser(String login, String password, Role role) throws IllegalArgumentException {
        if (login.isEmpty() || password.isEmpty()) {
            throw new IllegalArgumentException("Both login and password cannot be empty");
        }
        users.add(new User(login, DigestUtils.md5Hex(password), role));
    }

    @Override
    public User getUserByLogin(String login) {
        for (User user : users) {
            if (user.getLogin().equals(login)) {
                return user;
            }
        }
        return null;
    }

    @Override
    public List<User> getAllUsers() {
        return users;
    }
}