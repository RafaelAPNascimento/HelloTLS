package tls;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.net.ssl.SSLContext;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.net.HttpURLConnection.HTTP_OK;
import static java.net.http.HttpClient.Version.HTTP_1_1;

public class TlsTest {

    private static final Path CLIENT_TRUST_STORE = Paths.get("/home/rafael/Library/Practice/_02_httpsLocalHost/client.truststore");
    private static final Path LOCALHOST_CERTIFICATE = Paths.get("/home/rafael/Library/Practice/_02_httpsLocalHost/localhost");


    @Test
    public void shouldSuccessOverTLS() throws Exception {

        TLSUtil.loadCertificate(LOCALHOST_CERTIFICATE, CLIENT_TRUST_STORE);

        HttpClient httpClient = HttpClient.newBuilder().version(HTTP_1_1).build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://localhost:8443/hello-tls/api/service/Rafael"))
                .header("Content-Type", "application/json")
                .GET()
                .build();

        HttpResponse<String> resp = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(resp.statusCode(), HTTP_OK);
    }

    @Test
    public void shouldIgnoreTLS() throws Exception {

        SSLContext sslContext = TLSUtil.ignoreTLS();

        HttpClient httpClient = HttpClient.newBuilder()
                .sslContext(sslContext)
                .version(HTTP_1_1).build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://localhost:8443/hello-tls/api/service/Rafael"))
                .header("Content-Type", "application/json")
                .GET()
                .build();

        HttpResponse<String> resp = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(resp.statusCode(), HTTP_OK);
    }
}
