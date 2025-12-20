package pl.manager.library.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {
    private final String login;
    private final String password;
    private Role role;

    public User(String login, String password) {
        this.login = login;
        this.password = password;
        this.role = Role.USER;
    }

    public User (String login, String password, Role role) {
        this.login = login;
        this.password = password;
        this.role = role;
    }
}