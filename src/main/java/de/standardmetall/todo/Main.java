package de.standardmetall.todo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
	private static Scanner scanner = new Scanner(System.in);

	public static void main(String args[]) throws InterruptedException {
		System.out.println("--- TODO ---");
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
			}
			if (input.equalsIgnoreCase("C")) {
				askForTaskAndDelete();
				clearConsole();
			}
			if (input.equalsIgnoreCase("D")) {
				askForDeleteAll();
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
		System.out.println("Du hast folgende Aufgaben:");
		if (Todo.getTasks().size() == 0) {
			System.out.println("Keine");
			System.out.println(" ");
		} else {
			for (int i = 0; i < Todo.getTasks().size(); i++) {
				System.out.println("-> " + (i + 1) + ": " + Todo.getTasks().get(i).getDescription());
			}
			System.out.println(" ");
		}
	}

	// Zeigt die hilfe
	private static void printHelp() {
		System.out.println("--- TODO ---");
		System.out.println("A : Alle Aufgaben anzeigen");
		System.out.println("B : Aufgabe hinzufügen");
		System.out.println("C : Aufgabe entfernen");
		System.out.println("D : Alle Aufgaben löschen");
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
		if(input == "") {
			System.out.println("Bitte füge eine Beschreibung hinzu!");
			askForTaskAndAdd();
			return;
		}
		Todo todo = new Todo(input);
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

}
