package de.standardmetall.todo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Todo implements Comparable<Todo>{
	private String description;
	private Date date;
	private final static List<Todo> TASKS = new ArrayList<>();
	
	public Todo(String description, Date date) {
		this.setDescription(description);
		this.date = date;
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

	public Date getDate() {
		return date;
	}

	@Override
	public int compareTo(Todo o) {
		return date.compareTo(o.getDate());
	}
}
