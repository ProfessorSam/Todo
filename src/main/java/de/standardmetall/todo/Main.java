package de.standardmetall.todo;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Main {
	private static Scanner scanner = new Scanner(System.in);
	private static boolean autosave = true;

	public static void main(String args[]) throws InterruptedException {
		System.out.println("--- TODO ---");
		for(String string : args) {
			if(string.equalsIgnoreCase("-disableAutoSave")) {
				autosave = false;
			}
		}
		loadTodo();
		mainLoop();
	}	
	
	//Main process loop
	private static void mainLoop() {
		while (true) {
			clearConsole(false);
			printTasks();
			printHelp();
			String input = scanner.nextLine();
			if (input.equalsIgnoreCase("A")) {
				printTasks();
				printHelp();
				clearConsole(false);
			}
			if (input.equalsIgnoreCase("B")) {
				askForTaskAndAdd();
				clearConsole();
				autoSave();
			}
			if (input.equalsIgnoreCase("C")) {
				askForTaskAndDelete();
				clearConsole();
				autoSave();
			}
			if (input.equalsIgnoreCase("D")) {
				askForDeleteAll();
				clearConsole();
				autoSave();
			}
			if (input.equalsIgnoreCase("E") && !autosave) {
				System.out.println("Todo wird gespeichert");
				saveTodo();
				clearConsole();
			}
		}
	}
	//Console clearen
	private static void clearConsole(boolean delay) {
		try {
			if(delay == true) {
				Thread.sleep(1500);
			}
			new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	//selbe wie oben nur für Leute die zu fault für argumente sind
	private static void clearConsole() {
		clearConsole(true);
	}

	// Gibt alle aufgaben über sysout aus
	private static void printTasks() {
		sortTasks();
		System.out.println("Du hast folgende Aufgaben:");
		if (Todo.getTasks().size() == 0) {
			System.out.println("Keine");
			System.out.println(" ");
		} else {
			System.out.println(" ");
			for (int i = 0; i < Todo.getTasks().size(); i++) {
				if(Todo.getTasks().get(i).getDate().compareTo(Calendar.getInstance().getTime()) > 0) {
					System.out.println("-> " + (i + 1) + ") [" + DateFormat.getInstance().format(Todo.getTasks().get(i).getDate()) + "] " + Todo.getTasks().get(i).getDescription());
				}
				else {
					System.out.println("-> " + (i + 1) + ") [\u001B[31m" + DateFormat.getInstance().format(Todo.getTasks().get(i).getDate()) + "\u001B[0m] " + Todo.getTasks().get(i).getDescription());
				}
			}
			System.out.println(" ");
		}
	}
	
	//Sortiert Liste mit Todos nach datum neuste -> älteste
	private static void sortTasks() {
		ArrayList<Todo> tasks = new ArrayList<>(Todo.getTasks());
		Collections.sort(tasks);
		Todo.getTasks().clear();
		Todo.getTasks().addAll(tasks);
	}

	// Zeigt die hilfe
	private static void printHelp() {
		System.out.println("--- TODO ---");
		System.out.println("A : Alle Aufgaben anzeigen");
		System.out.println("B : Aufgabe hinzufügen");
		System.out.println("C : Aufgabe entfernen");
		System.out.println("D : Alle Aufgaben löschen");
		if(!autosave) {
			System.out.println("E : Aufgaben speichern");
		}
		System.out.println(" ");
	}
	
	//Fragt nach einer weiteren Aufgabe und fügt sie hinzu
	private static void askForTaskAndAdd() {
		System.out.println(" ");
		System.out.println("Wie lautet die Aufgabe?");
		String input = scanner.nextLine();
		if(input == null) {
			System.out.println("Bitte füge eine Beschreibung hinzu!");
			askForTaskAndAdd();
			return;
		}
		if(input == "" || input == " ") {
			System.out.println("Bitte füge eine Beschreibung hinzu!");
			askForTaskAndAdd();
			return;
		}
		System.out.println("Bis wann soll die Aufgabe erledigt sein?");
		String dateInput = scanner.nextLine();
		Date date = Calendar.getInstance().getTime();
		try {
			date = DateFormat.getInstance().parse(dateInput);
		} catch (ParseException e) {
			System.out.println("Bit gibt das Datum wie folgt ein: dd.mm.yyyy mm.hh");
			clearConsole();
			askForTaskAndAdd();
			return;
		}
		if(date.compareTo(Calendar.getInstance().getTime()) < 0) {
			System.out.println("Das Datum muss in der Zukunft liegen!");
			clearConsole(true);
			printTasks();
			askForTaskAndAdd();
			return;
		}
		Todo todo = new Todo(input, date);
		Todo.getTasks().add(todo);
		System.out.println("Aufgabe erfolgreich hinzugefügt!");
	}
	
	//Wartet auf eingabe von Aufgabennummer und löscht anschließend
	private static void askForTaskAndDelete() {
		printTasks();
		System.out.println(" ");
		System.out.println("Welche Aufgabe solle gelöscht werden?");
		String input = scanner.nextLine();
		int task = -1;
		try {
			task = Integer.parseInt(input);
		} catch (Exception e) {
			System.out.println("Bitte gib eine Zahl an!");
		}
		if(task == -1) {
			askForTaskAndDelete();
			return;
		}
		if(task <= 0 || task >= (Todo.getTasks().size() + 1)) {
			System.out.println("Bitte gib eine zahl von 1 bis " + (Todo.getTasks().size()));
			askForTaskAndDelete();
			return;
		}
		task--;
		Todo.getTasks().remove(task);
		System.out.println("Die Aufgabe wurde entfernt");
	}
	
	//Löscht alle aufgaben
	private static void askForDeleteAll() {
		System.out.println(" ");
		System.out.println("Möchtest du wirklich fortfahren? (Ja|Nein)");
		String input = scanner.nextLine();
		if(input.equalsIgnoreCase("Ja")) {
			ArrayList<Todo> tasks = new ArrayList<Todo>(Todo.getTasks());
			for(Todo todo: tasks) {
				Todo.getTasks().remove(todo);
			}
			System.out.println("Alle Aufgaben gelöscht!");
			System.out.println(" ");
			return;
		}
		System.out.println("Löschen abgebrochen");
	}
	
	//Speichert Todo in todo.txt
	private static void saveTodo() {
		File file = new File("todo.txt");
		if(file.exists()) {
			file.delete();
		}
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			for(Todo todo : Todo.getTasks()) {
				writer.write(DateFormat.getInstance().format(todo.getDate()) + ";" +todo.getDescription());
				writer.newLine();
			}
			writer.close();
		} catch (IOException e) {
			System.out.println("Todoliste konnte nicht gespeichert werden");
		}
	}
	
	//Lädt todo aus todo.txt
	private static void loadTodo() {
		File file = new File("todo.txt");
		try {
			URI uri = file.toURI();
			List<String> lines = Files.readAllLines(Paths.get(uri), StandardCharsets.UTF_8);
			for(String string : lines) {
				Date date = DateFormat.getInstance().parse(string.split(";")[0]);
				Todo.getTasks().add(new Todo(string.split(";", 2)[1], date));
			}
		} catch (FileNotFoundException e) {
			System.out.println("Todoliste konnte nicht geladen werden " + e.getLocalizedMessage());
		} catch (IOException e) {
			System.out.println("Todoliste konnte nicht geladen werden " + e.getLocalizedMessage());
		} catch (ParseException e) {
			System.out.println("Todoliste konnte nicht geladen werden " + e.getLocalizedMessage());
		}
	}
	
	//Überprüft ob autosave an ist und speichert
	private static void autoSave() {
		if(autosave) {
		saveTodo();
		}
	}

}
