package tls;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

public class CertificateLoader {

    static void loadCertificate(Path serverCertificatePath, Path clientTrustStorePath) throws Exception {

        if (!Files.exists(clientTrustStorePath))
            createClientTrustStore(clientTrustStorePath);

        String alias = "wildfly23.localhost";
        String password = "changeit";

        // to load a new truststore other than default cacerts
        KeyStore clientTrustStore = KeyStore.getInstance(KeyStore.getDefaultType());
        InputStream in = Files.newInputStream(clientTrustStorePath);
        clientTrustStore.load(in, password.toCharArray());
        in.close();

        // CertficateFactory to create a new reference to the server certificate file
        CertificateFactory cf = CertificateFactory.getInstance("X.509");

        // read the server certificate
        InputStream serverCertstream = Files.newInputStream(serverCertificatePath);

        // certificate instance
        Certificate serverCertificate =  cf.generateCertificate(serverCertstream);

        // add the server certificate to our newly truststore
        clientTrustStore.setCertificateEntry(alias, serverCertificate);

        // save modifications
        OutputStream out = Files.newOutputStream(clientTrustStorePath);
        clientTrustStore.store(out, password.toCharArray());
        out.close();

        // dynamically set default truststore for this application from cacerts to newly client.truststore
        System.setProperty("javax.net.ssl.trustStore", clientTrustStorePath.toString());
        System.setProperty("javax.net.ssl.trustStorePassword", password);
    }

    private static void createClientTrustStore(Path clientTrustStorePath) throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException {

        KeyStore clientTrustStore = KeyStore.getInstance(KeyStore.getDefaultType());
        char[] password = "changeit".toCharArray();

        clientTrustStore.load(null, password);

        OutputStream fos = Files.newOutputStream(clientTrustStorePath);
        clientTrustStore.store(fos, password);
        fos.close();
    }
}
