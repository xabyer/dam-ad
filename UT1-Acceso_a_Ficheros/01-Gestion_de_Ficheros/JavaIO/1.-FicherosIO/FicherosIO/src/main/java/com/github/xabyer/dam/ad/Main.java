package com.github.xabyer.dam.ad;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    static void main() {
        try {
            writeTextToFile();
            readTextFromFile();
        } catch (IOException e) {
            e.printStackTrace(System.err);
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    //Exercise 1
    // Request a text message from the console and write it to a text file. Use BufferedWriter to do this.
    public static void writeTextToFile() throws IOException {
        String text = IO.readln("Introduce a text: ");

        BufferedWriter writeText = new BufferedWriter(new FileWriter("text.txt", true));
        writeText.write(text + "\r\n");

        writeText.close();
    }

    //Exercise 2
    // Read ebook.txt using BufferReader
    public static void readTextFromFile() throws IOException {

        BufferedReader readEbookList = new BufferedReader(new FileReader("ebookList.txt"));
        String line = "";
        while ((line = readEbookList.readLine()) != null) {
            IO.println(line);
        }

        readEbookList.close();
    }
}


