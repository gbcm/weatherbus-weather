package io.pivotal;

import org.mockito.ArgumentMatcher;
import retrofit.client.Request;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class TestUtilities {
    public static FileReader fixtureReader(String fixture) throws FileNotFoundException {
        return new FileReader("src/test/resources/input/" + fixture + ".json");
    }

    public static String jsonFileToString(String path) throws IOException {
        StringBuilder sb = new StringBuilder();
        Stream<String> stream = Files.lines(Paths.get(path));
        stream.forEach(x -> sb.append(x));
        stream.close();
        return sb.toString();
    }

    public static class RequestWithUrl extends ArgumentMatcher<Request> {
        private String expectedUrl;

        public RequestWithUrl(String expectedUrl) {
            this.expectedUrl = expectedUrl;
        }

        @Override
        public boolean matches(Object request) {
            if (!(request instanceof Request)) return false;
            return ((Request) request).getUrl().equals(expectedUrl);
        }
    }
}
