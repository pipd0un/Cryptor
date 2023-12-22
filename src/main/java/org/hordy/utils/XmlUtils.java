package org.hordy.utils;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.StringWriter;
import java.nio.file.Path;

public class XmlUtils {

    public void saveXmlDocument(org.w3c.dom.Document document, String outputXmlPath) throws Exception {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();

        // Add indentation and omit XML declaration to make it more readable
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");

        transformer.transform(new DOMSource(document), new StreamResult(new FileOutputStream(outputXmlPath)));
    }

    public String getEncryptedXmlPath(String xmlPath) {
        Path path = Path.of(xmlPath);
        System.out.println(path);
        String fileName = path.getFileName().toString();
        if(path.getParent() != null) {
            String directory = path.getParent().toString();
            String baseName = fileName.substring(0, fileName.lastIndexOf('.'));
            String extension = fileName.substring(fileName.lastIndexOf('.'));
            return directory + File.separator + baseName + "_encrypted" + extension;
        }
        else{
            String baseName = fileName.substring(0, fileName.lastIndexOf('.'));
            String extension = fileName.substring(fileName.lastIndexOf('.'));
            return baseName + "_encrypted" + extension;
        }
    }

    public byte[] serializeDocument(org.w3c.dom.Document document) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.transform(new DOMSource(document), new StreamResult(baos));

        return baos.toByteArray();
    }

    public void printDocument(org.w3c.dom.Document document) throws Exception {
        StringWriter writer = new StringWriter();
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.transform(new DOMSource(document), new StreamResult(writer));

        System.out.println(writer);
    }
}
