package tls;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

public class TLSUtil {

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

        // loads the given trust store; to create a new one, pass null
        clientTrustStore.load(null, password);

        // creates a new file
        OutputStream fos = Files.newOutputStream(clientTrustStorePath);

        // stores the given keysore stream into the streaml, protected by the given password
        clientTrustStore.store(fos, password);
        fos.close();
    }

    public static SSLContext ignoreTLS() throws KeyManagementException, NoSuchAlgorithmException {

        SSLContext sslcontext = SSLContext.getInstance("TLS");

        sslcontext.init(null, new TrustManager[]{new X509TrustManager() {

                public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {}

                public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {}

                public X509Certificate[] getAcceptedIssuers() { return new X509Certificate[0]; }

            }},
            new java.security.SecureRandom()
        );

        return sslcontext;
    }
}
