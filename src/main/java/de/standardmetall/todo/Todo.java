package de.standardmetall.todo;

import java.util.ArrayList;
import java.util.List;

public class Todo {
	private String description;
	private final static List<Todo> TASKS = new ArrayList<>();
	
	public Todo(String description) {
		this.setDescription(description);
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public static List<Todo> getTasks() {
		return TASKS;
	}
}
