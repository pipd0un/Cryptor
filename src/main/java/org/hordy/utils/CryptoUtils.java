package org.hordy.utils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import java.security.PublicKey;


public class CryptoUtils {

    public byte[] encryptXmlContent(org.w3c.dom.Document document, SecretKey symmetricKey) throws Exception {

        XmlUtils xmlUtils = new XmlUtils();
        // Use Cipher.getInstance("DESede/CBC/PKCS5Padding") for TripleDES encryption

        Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, symmetricKey);

        // Assuming 'data' is the byte array representation of your XML content
        byte[] data = xmlUtils.serializeDocument(document);
        return cipher.doFinal(data);
    }

    public byte[] encryptSymmetricKey(SecretKey symmetricKey, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher.doFinal(symmetricKey.getEncoded());
    }


}
