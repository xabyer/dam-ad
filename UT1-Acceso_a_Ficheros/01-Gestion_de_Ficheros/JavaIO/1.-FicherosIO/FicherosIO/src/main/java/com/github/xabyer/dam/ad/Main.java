package com.github.xabyer.dam.ad;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    static void main() {
        String text = IO.readln("Introduce a text: ");

        try(
                BufferedWriter writeText = new BufferedWriter(new FileWriter("text.txt"))
        ) {
            writeText.write(text + "\r\n");

        } catch (IOException e) {
            e.printStackTrace(System.err);
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, e);
        }
    }
}
