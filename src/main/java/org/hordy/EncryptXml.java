package org.hordy;

import org.hordy.model.*;
import org.hordy.utils.*;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.security.*;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;


public class EncryptXml {

    // example shell cmd
    // EncryptXml.exe{.jar} [pathToBeEncrypted.xml] [x509cert.pem] [-si]
    // this usage encrypts xml as Slovenian template needs

    public static void main(String[] args) {
        String xmlPath = args[0];
        String certPath = args[1];

        if(args.length > 3){
            System.out.println("[ERROR] Many args!");
            System.exit(1);
        }else if(args.length < 3){
            System.out.println("[ERROR] Missing args!");
            System.exit(1);
        }

        Security.addProvider(new BouncyCastleProvider());

        CryptoUtils cryptoUtils = new CryptoUtils();
        XmlUtils xmlUtils = new XmlUtils();


        try {
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            org.w3c.dom.Document document = docBuilder.parse(new FileInputStream(xmlPath));


            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            X509Certificate certificate = (X509Certificate) certificateFactory.generateCertificate(new FileInputStream(certPath));

            PublicKey publicKey = certificate.getPublicKey();

            KeyGenerator keyGenerator = KeyGenerator.getInstance("DESede");
            SecretKey symmetricKey = keyGenerator.generateKey();

            byte[] encryptedXmlContent = cryptoUtils.encryptXmlContent(document, symmetricKey);

            byte[] encryptedSymmetricKey = cryptoUtils.encryptSymmetricKey(symmetricKey, publicKey);

            org.w3c.dom.Document encryptedDocument = null;

            if(args[2].equals("-si")){
                SI slovenianXml = new SI();
                slovenianXml.fill(encryptedXmlContent, encryptedSymmetricKey, certificate);

                encryptedDocument = slovenianXml.getXML();
            }else{
                System.out.println("[ERROR] Missing argument! (ie. -si)");
                System.exit(0);
            }


            // Determine the output XML path
            String outputXmlPath = xmlUtils.getEncryptedXmlPath(xmlPath);

            // Save the encrypted XML
            xmlUtils.saveXmlDocument(encryptedDocument, outputXmlPath);

            System.out.println("Encryption completed successfully. Encrypted XML saved at: " + outputXmlPath);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
