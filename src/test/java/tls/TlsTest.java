package tls;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static java.net.HttpURLConnection.HTTP_OK;
import static java.net.http.HttpClient.Version.HTTP_1_1;

public class TlsTest {

    private static final String CLIENT_TRUST_STORE_PATH = "/home/rafael/Library/Practice/_02_httpsLocalHost/client.truststore";
    private static final String LOCALHOST_CERTIFICATE_PATH = "/home/rafael/Library/Practice/_02_httpsLocalHost/localhost";

    @BeforeAll
    public static void init() throws Exception {

        CertificateLoader.loadCertificate(LOCALHOST_CERTIFICATE_PATH, CLIENT_TRUST_STORE_PATH);
    }

    @Test
    public void shouldSuccessOverTLS() throws IOException, InterruptedException {

        HttpClient httpClient = HttpClient.newBuilder().version(HTTP_1_1).build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://localhost:8443/hello-tls/api/service/Rafael"))
                .header("Content-Type", "application/json")
                .GET()
                .build();

        HttpResponse<String> resp = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(resp.statusCode(), HTTP_OK);
    }
}
