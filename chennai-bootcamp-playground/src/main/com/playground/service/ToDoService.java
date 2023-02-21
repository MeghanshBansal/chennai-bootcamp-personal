package main.com.playground.service;

import main.com.playground.domain.ToDo;

import java.sql.SQLException;

public class ToDoService {
    String user;
    String message;

    public ToDoService(String user, String message) {
        this.user = user;
        this.message = message;
    }

    public String getUser() {
        return user;
    }

    public String getMessage() {
        return message;
    }

    public void saveToDo(int userId) throws ClassNotFoundException, SQLException {
        ToDo todo = new ToDo(this.user, this.message);
        todo.save(userId);
    }
}
