import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import javafx.stage.Stage;

public class start {
    public static void main(String[] args) {
        Window.main(args);

    }

    /*
     * public static final String API_KEY =
     * "92c8655787msh7ea35a1c5b792ebp1e18dbjsne33e6f59238a";
     * 
     * public static void getrequest(String[] aeStrings) throws IOException,
     * InterruptedException {
     * HttpRequest request = HttpRequest.newBuilder()
     * .uri(URI.create(
     * "https://weatherapi-com.p.rapidapi.com/current.json?q=53.1%2C-0.13"))
     * .header("X-RapidAPI-Key", API_KEY)
     * .header("X-RapidAPI-Host", "weatherapi-com.p.rapidapi.com")
     * .method("GET", HttpRequest.BodyPublishers.noBody())
     * .build();
     * HttpResponse<String> response = HttpClient.newHttpClient().send(request,
     * HttpResponse.BodyHandlers.ofString());
     * System.out.println(response.body());
     * 
     * }
     */

}
