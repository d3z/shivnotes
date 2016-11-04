/*
 * Copyright 2016 Instil Software.
 */
package com.deloitte.shivnotes;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class ShivNotes {

    private static final String NOTE_FIELD_SEPARATOR = ";";

    private final String notesFilename;
    private final InputStream in;
    private final PrintStream out;
    private final Scanner scanner;
    private final List<Note> notes = new ArrayList<Note>();

    public static void main(String[] args) {
        new ShivNotes("notes.txt", System.in, System.out).start();
    }

    public ShivNotes(String notesFilename, InputStream in, PrintStream out) {
        this.notesFilename = notesFilename;
        this.in = in;
        this.out = out;
        this.scanner = new Scanner(in);
    }

    public void start() {
        loadNotes();
        startLoop();
    }

    private void loadNotes() {
        if (notesFileDoesNotExistYet()) {
            return;
        }
        BufferedReader notesFileReader = null;
        try {
            notesFileReader = new BufferedReader(new FileReader(notesFilename));
            String noteLine;
            while(null != (noteLine = notesFileReader.readLine())) {
                if (!noteLine.isEmpty()) {
                    Note note = makeNoteFrom(noteLine);
                    notes.add(note);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (notesFileReader != null) {
                try {
                    notesFileReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private boolean notesFileDoesNotExistYet() {
        File notesFile = new File(notesFilename);
        return !notesFile.exists();
    }

    private Note makeNoteFrom(String noteLine) {
        String[] noteLineParts = noteLine.split(NOTE_FIELD_SEPARATOR);
        return new Note(noteLineParts[0], noteLineParts[1], noteLineParts[2]);
    }

    private void saveNotes() {
        BufferedWriter notesWriter = null;
        try {
            notesWriter = new BufferedWriter(new FileWriter(notesFilename));
            writeAllNotesTo(notesWriter);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (notesWriter != null) {
                try {
                    notesWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void writeAllNotesTo(BufferedWriter notesWriter) throws IOException {
        for (Note note: notes) {
            String noteLineToWrite = makeNoteLineFor(note);
            notesWriter.write(noteLineToWrite);
            notesWriter.newLine();
        }
    }

    private String makeNoteLineFor(Note note) {
        return String.format("%s;%s;%s", note.getTitle(), note.getText(), note.getDate());
    }

    private void startLoop() {
        printIntroduction();
        while(true) {
            printMenu();
            String command = readUserChoice();
            if (userWantsToExit(command)) {
                break;
            }
            performUserCommand(command);
        }
        saveNotes();
        printExitMessage();
    }

    private void performUserCommand(String command) {
        if (userWantsToAddANote(command)) {
            Note note = createNoteForUserInput();
            notes.add(note);
        } else if (userWantsToListAllNotes(command)) {
            listAllNotes();
        } else {
            printUnknownCommandMessage();
        }
    }

    private Note createNoteForUserInput() {
        String title = askUserForNoteTitle();
        String text = askUserForNoteText();
        String date = new Date().toString();
        return new Note(title, text, date);
    }

    private String askUserForNoteTitle() {
        this.out.print("Note title: ");
        return readInputFromUser();
    }

    private String askUserForNoteText() {
        this.out.print("Note text: ");
        return readInputFromUser();
    }

    private String readInputFromUser() {
        return this.scanner.nextLine();
    }

    private void printExitMessage() {
        this.out.println("Thanks. Bye.");
    }

    private boolean userWantsToExit(String command) {
        return command.toLowerCase().equals("x");
    }

    private boolean userWantsToAddANote(String command) {
        return command.equals("1");
    }

    private boolean userWantsToListAllNotes(String command) {
        return command.equals("2");
    }

    private void printUnknownCommandMessage() {
        this.out.println("### Unknown command ###");
    }

    private void listAllNotes() {
        for (Note note : notes) {
            this.out.println(note);
            this.out.println();
        }
    }

    private void printIntroduction() {
        this.out.println("********************");
        this.out.println("Welcome to ShivNotes");
        this.out.println("********************");
    }

    private void printMenu() {
        this.out.println("1. Add new note");
        this.out.println("2. List all notes");
        this.out.println("X. Exit");
        this.out.print("> ");
    }

    private String readUserChoice() {
        return this.scanner.nextLine();
    }

}
