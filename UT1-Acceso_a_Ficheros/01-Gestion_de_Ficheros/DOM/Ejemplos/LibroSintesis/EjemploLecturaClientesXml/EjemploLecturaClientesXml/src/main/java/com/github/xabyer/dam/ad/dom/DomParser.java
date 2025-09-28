package com.github.xabyer.dam.ad.dom;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.PrintStream;

public class DomParser {

    private static final String INDENT_LEVEL = " "; // For Indentation.

    public static void showNode(Node node, int level, PrintStream printStream) {
        if(node.getNodeType() == Node.TEXT_NODE) {
            String text = node.getNodeValue();
            if(text.trim().isEmpty())
                return;
        }
        for (int i = 0; i < level; i++) {
            printStream.print(INDENT_LEVEL); //Indentation
        }

        switch (node.getNodeType()) {
            case Node.DOCUMENT_NODE -> {
                Document document = (Document) node;
                printStream.println(
                        "Documento DOM, version: " +
                        document.getXmlVersion() + ", codificación: " +
                        document.getXmlEncoding()
                );
            }
            case Node.ELEMENT_NODE -> {
                printStream.print("<" + node.getNodeName());
                NamedNodeMap attributeList = node.getAttributes();
                for (int i = 0; i < attributeList.getLength(); i++) {
                    Node attribute = attributeList.item(i);
                    printStream.print("@ " +attribute.getNodeName() + "[" +  attribute.getNodeValue() + "]");

                }
                printStream.println(">");
            }
            case Node.TEXT_NODE -> {
                printStream.println(node.getNodeName() + "[" + node.getNodeValue() + "]");
                if(level > 0) {
                    for (int i = 0; i < level - 1; i++) {
                        printStream.print(" ");
                    }
                }
                printStream.println("</" + node.getParentNode().getNodeName() + ">");
            }
            default -> printStream.print("(nodo de tipo: " + node.getNodeType() + ")" );
        }
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            showNode(childNodes.item(i), level + 1, printStream);
        }
    }

}
