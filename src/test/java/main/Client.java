package main;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

import static java.net.http.HttpClient.Version.HTTP_1_1;

public class Client {

    private static final String CLIENT_TRUST_STORE_PATH = "";
    private static final String LOCALHOST_CERTIFICATE_PATH = "";

    public static void main(String[] args) throws Exception {

        //loadCertificate(LOCALHOST_CERTIFICATE_PATH, CLIENT_TRUST_STORE_PATH);
        request();
    }

    private static void request() throws IOException, InterruptedException {

        HttpClient httpClient = HttpClient.newBuilder().version(HTTP_1_1).build();

        HttpRequest request = HttpRequest.newBuilder()
                //.uri(URI.create("https://localhost:8443/hello-authentication/api/auth/connect/token"))
                .uri(URI.create("http://localhost:8080/hello-tls/api/service/Rafael"))
                .header("Content-Type", "application/json")
                .GET()
                .build();

        HttpResponse<String> resp = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(resp.body());
    }

    private static void loadCertificate(String serverCertificatePath, String clientTrustStorePath) throws Exception {

        if (!Files.exists(Path.of(clientTrustStorePath)))
            createClientTrustStore(clientTrustStorePath);

        String alias = "wildfly18.localhost";
        String password = "changeit";

        // to load a new truststore other than default cacerts
        KeyStore clienTruststore = KeyStore.getInstance(KeyStore.getDefaultType());
        FileInputStream in = new FileInputStream(clientTrustStorePath);
        clienTruststore.load(in, password.toCharArray());
        in.close();

        // CertficateFactory to create a new reference to the server certificate file
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        // read the server certificate
        InputStream serverCertstream = new FileInputStream(serverCertificatePath);
        // certificate instance
        Certificate serverCertificate =  cf.generateCertificate(serverCertstream);

        // add the server certificate to our newly truststore
        clienTruststore.setCertificateEntry(alias, serverCertificate);

        // save modifications
        FileOutputStream out = new FileOutputStream(clientTrustStorePath);
        clienTruststore.store(out, password.toCharArray());
        out.close();

        // dynamically set default truststore for this application from cacerts to client.truststore
        System.setProperty("javax.net.ssl.trustStore", clientTrustStorePath);
        System.setProperty("javax.net.ssl.trustStorePassword", password);
    }

    private static void createClientTrustStore(String clientTrustStorePath) throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException {

        KeyStore clientTrustStore = KeyStore.getInstance(KeyStore.getDefaultType());
        char[] password = "changeit".toCharArray();

        clientTrustStore.load(null, password);

        FileOutputStream fos = new FileOutputStream(clientTrustStorePath);
        clientTrustStore.store(fos, password);
        fos.close();
    }

}
