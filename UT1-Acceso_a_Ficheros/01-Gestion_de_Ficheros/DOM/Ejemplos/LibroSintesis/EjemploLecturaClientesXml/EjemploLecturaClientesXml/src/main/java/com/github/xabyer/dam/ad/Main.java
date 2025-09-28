package com.github.xabyer.dam.ad;

import com.github.xabyer.dam.ad.dom.DomParser;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;


public class Main {
    static void main() {
        DocumentBuilderFactory readXmlClients = DocumentBuilderFactory.newDefaultInstance();
        readXmlClients.setIgnoringComments(true);
        readXmlClients.setIgnoringElementContentWhitespace(true);

        var xmlClientsPath = getPath();

        if (xmlClientsPath.isEmpty()){
            IO.println("Debe indicar el nombr del fichero, por favor.");
            return;
        }

        DocumentBuilder documentBuilder;
        try {
            documentBuilder = readXmlClients.newDocumentBuilder();
            Document domClientsDocument = documentBuilder.parse(new File(xmlClientsPath));
            DomParser.showNode(domClientsDocument, 0, System.out);
        } catch (IOException | IllegalArgumentException | ParserConfigurationException | SAXException e) {
            System.err.println(e.getMessage());

        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }

    private static String getPath () {
        return IO.readln("Introduce la ruta incluyendo el nombre del archivo xml a leer: ");
    }
}
