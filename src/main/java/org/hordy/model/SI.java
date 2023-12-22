package org.hordy.model;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;

public class SI {

   private final Document _template;
   private final Element _modulusElement;
   private final Element _exponentElement;
   private final Element _x509IssuerNameElement;
   private final Element _x509SerialNumberElement;
   private final Element _x509SubjectNameElement;
   private final Element _x509CertificateElement;
   private final Element _cipherValueElement;
   private final Element _cipherValueElementContent;
   private boolean _filled = false;

   public SI() throws ParserConfigurationException {
       DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
       DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
       org.w3c.dom.Document encryptedDocument = documentBuilder.newDocument();

       // Create the EncryptedData structure
       Element encryptedDataElement = encryptedDocument.createElementNS("http://www.w3.org/2001/04/xmlenc#", "EncryptedData");
       encryptedDataElement.setAttribute("Type", "http://www.w3.org/2001/04/xmlenc#Element");
       encryptedDataElement.setAttribute("xmlns", "http://www.w3.org/2001/04/xmlenc#");
       encryptedDataElement.setAttribute("xmlns:xenc", "http://www.w3.org/2001/04/xmlenc#");
       encryptedDataElement.setAttribute("xmlns:ds", "http://www.w3.org/2000/09/xmldsig#");

       Element encryptionMethodElement = encryptedDocument.createElement("EncryptionMethod");
       encryptionMethodElement.setAttribute("Algorithm", "http://www.w3.org/2001/04/xmlenc#tripledes-cbc");

       Element keyInfoElement = encryptedDocument.createElementNS("http://www.w3.org/2000/09/xmldsig#", "ds:KeyInfo");

       Element encryptedKeyElement = encryptedDocument.createElement("EncryptedKey");
       Element keyEncryptionMethodElement = encryptedDocument.createElement("EncryptionMethod");
       keyEncryptionMethodElement.setAttribute("Algorithm", "http://www.w3.org/2001/04/xmlenc#rsa-1_5");

       Element rsaKeyValueElement = encryptedDocument.createElementNS("http://www.w3.org/2000/09/xmldsig#", "ds:RSAKeyValue");
       Element modulusElement = encryptedDocument.createElement("ds:Modulus");
       Element exponentElement = encryptedDocument.createElement("ds:Exponent");

       rsaKeyValueElement.appendChild(modulusElement);
       rsaKeyValueElement.appendChild(exponentElement);

       Element x509DataElement = encryptedDocument.createElement("ds:X509Data");
       Element x509IssuerSerialElement = encryptedDocument.createElement("ds:X509IssuerSerial");
       Element x509IssuerNameElement = encryptedDocument.createElement("ds:X509IssuerName");
       Element x509SerialNumberElement = encryptedDocument.createElement("ds:X509SerialNumber");

       x509IssuerSerialElement.appendChild(x509IssuerNameElement);
       x509IssuerSerialElement.appendChild(x509SerialNumberElement);

       x509DataElement.appendChild(x509IssuerSerialElement);

       Element x509SubjectNameElement = encryptedDocument.createElement("ds:X509SubjectName");

       x509DataElement.appendChild(x509SubjectNameElement);

       Element x509CertificateElement = encryptedDocument.createElement("ds:X509Certificate");

       x509DataElement.appendChild(x509CertificateElement);

       keyInfoElement.appendChild(encryptedKeyElement);
       encryptedKeyElement.appendChild(keyEncryptionMethodElement);
       encryptedKeyElement.appendChild(rsaKeyValueElement);
       encryptedKeyElement.appendChild(x509DataElement);

       Element cipherDataElement = encryptedDocument.createElement("CipherData");
       Element cipherValueElement = encryptedDocument.createElement("CipherValue");

       cipherDataElement.appendChild(cipherValueElement);

       encryptedKeyElement.appendChild(cipherDataElement);

       Element cipherDataElementContent = encryptedDocument.createElement("CipherData");
       Element cipherValueElementContent = encryptedDocument.createElement("CipherValue");

       cipherDataElementContent.appendChild(cipherValueElementContent);

       encryptedDataElement.appendChild(encryptionMethodElement);
       encryptedDataElement.appendChild(keyInfoElement);
       encryptedDataElement.appendChild(cipherDataElementContent);

       encryptedDocument.appendChild(encryptedDataElement);
       _template = encryptedDocument;
       _modulusElement = modulusElement;
       _exponentElement = exponentElement;
       _x509CertificateElement = x509CertificateElement;
       _x509IssuerNameElement = x509IssuerNameElement;
       _x509SerialNumberElement = x509SerialNumberElement;
       _x509SubjectNameElement = x509SubjectNameElement;
       _cipherValueElement = cipherValueElement;
       _cipherValueElementContent = cipherValueElementContent;
   }

   public void fill(byte[] encryptedXmlContent, byte[] encryptedSymmetricKey, X509Certificate certificate) throws CertificateEncodingException {
       RSAPublicKey rsaPublicKey = (RSAPublicKey) certificate.getPublicKey();

       _modulusElement.setTextContent(Base64.getEncoder().encodeToString(rsaPublicKey.getModulus().toByteArray()));
       _exponentElement.setTextContent(Base64.getEncoder().encodeToString(rsaPublicKey.getPublicExponent().toByteArray()));

       _x509IssuerNameElement.setTextContent(certificate.getIssuerX500Principal().getName());
       _x509SerialNumberElement.setTextContent(certificate.getSerialNumber().toString());

       _x509SubjectNameElement.setTextContent(certificate.getSubjectX500Principal().getName());

       _x509CertificateElement.setTextContent(Base64.getEncoder().encodeToString(certificate.getEncoded()));

       _cipherValueElement.setTextContent(Base64.getEncoder().encodeToString(encryptedSymmetricKey));

       _cipherValueElementContent.setTextContent(Base64.getEncoder().encodeToString(encryptedXmlContent));

       _filled = true;
   }

   public Document getXML(){
       if(_filled){
           return _template;
       }else{
           return null;
       }

   }

}
