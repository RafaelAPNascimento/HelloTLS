package tls;

import javax.net.ssl.HttpsURLConnection;
import java.net.URL;
import java.security.cert.Certificate;

public class OnlyMemoryLoader {

    public static void main(String[] args) throws Exception {

        OnlyMemoryLoader om = new OnlyMemoryLoader();
        om.loadCertificate("https://localhost:8443");
    }

    public void loadCertificate(String host) throws Exception {

        URL destinationURL = new URL(host);
        HttpsURLConnection conn = (HttpsURLConnection) destinationURL.openConnection();
        conn.connect();
        Certificate[] certs = conn.getServerCertificates();
        System.out.println("nb = " + certs.length);
    }
}
