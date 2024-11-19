package hexlet.code;

import hexlet.code.model.Url;
import hexlet.code.repository.UrlChecksRepository;
import hexlet.code.repository.UrlsRepository;
import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

class AppTest {

    private static Javalin app;
    private static MockWebServer mockServer;

    private static Path getFixturePath(String fileName) {
        return Paths.get("src", "test", "resources", "fixtures", fileName)
                .toAbsolutePath().normalize();
    }

    private static String readFixture(String fileName) throws IOException {
        Path filePath = getFixturePath(fileName);
        return Files.readString(filePath).trim();
    }

    @BeforeAll
    public static void beforeAll() throws IOException {
        mockServer = new MockWebServer();
        MockResponse mockedResponse = new MockResponse()
                .setBody(readFixture("index.html"));
        mockServer.enqueue(mockedResponse);
        mockServer.start();
    }

    @AfterAll
    public static void afterAll() throws IOException {
        mockServer.shutdown();
    }

    @BeforeEach
    void setUp() throws SQLException, IOException {
        app = App.getApp();
    }


    @Nested
    class RootTest {

        @Test
        void testIndex() {
            JavalinTest.test(app, (server, client) -> {
                assertThat(client.get("/").code()).isEqualTo(toInt("200"));
            });
        }
    }

    @Nested
    class UrlTest {

        @Test
        void testIndex() {
            JavalinTest.test(app, (server, client) -> {
                assertThat(client.get("/urls").code()).isEqualTo(toInt("200"));
            });
        }

        @Test
        void testShow() throws SQLException {
            var url = new Url("http://test.io");
            UrlsRepository.save(url);
            JavalinTest.test(app, (server, client) -> {
                var responce = client.get("/urls/" + url.getId());
                assertThat(responce.code()).isEqualTo(toInt("200"));
            });
        }

        @Test
        void testStore() throws SQLException {
            String inputUrl = "https://ru.hexlet.io";

            JavalinTest.test(app, (server, client) -> {
                var requestBody = "url=" + inputUrl;
                var response = client.post("/urls", requestBody);
                assertThat(response.code()).isEqualTo(toInt("200"));
            });

            Url actualUrl = UrlsRepository.findByName(inputUrl).orElse(null);

            assertThat(actualUrl).isNotNull();
            assertThat(actualUrl.getName()).isEqualTo(inputUrl);
        }
    }

    @Nested
    class UrlCheckTest {

        @Test
        void testStore() throws SQLException {
            String url = mockServer.url("/").toString().replaceAll("/$", "");

            JavalinTest.test(app, (server, client) -> {
                var requestBody = "url=" + url;
                assertThat(client.post("/urls", requestBody).code()).isEqualTo(toInt("200"));

                var actualUrl = UrlsRepository.findByName(url).orElse(null);

                assertThat(actualUrl).isNotNull();
                assertThat(actualUrl.getName()).isEqualTo(url);

                client.post("/urls/" + actualUrl.getId() + "/checks");

                var responce = client.get("/urls/" + actualUrl.getId());
                assertThat(responce.code()).isEqualTo(toInt("200"));
                assertThat(responce.body().string()).contains(url);

                var actualCheckUrl = UrlChecksRepository
                        .findLatestChecks().get(actualUrl.getId());

                assertThat(actualCheckUrl).isNotNull();
                assertThat(actualCheckUrl.getStatusCode()).isEqualTo(toInt("200"));
                assertThat(actualCheckUrl.getTitle()).isEqualTo("Test page");
                assertThat(actualCheckUrl.getH1()).isEqualTo("Do not expect a miracle, miracles yourself!");
                assertThat(actualCheckUrl.getDescription()).contains("statements of great people");
            });
        }
    }

    private int toInt(String val) {
        return Integer.parseInt(val);
    }
}
