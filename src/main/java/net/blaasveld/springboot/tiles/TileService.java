package net.blaasveld.springboot.tiles;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class TileService {

    public InputStream getTile(int z, int x, int y) {
        System.out.println(String.format("Getting tile %d %d %d", z, x, y));
        return this.fetchTile(z, x, y);
    }

    private InputStream fetchTile(int z, int x, int y) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            URI uri = new URI(String.format("https://tile.openstreetmap.org/%d/%d/%d.png", z, x, y));

            System.out.println(String.format("Fetching tile from %s", uri.toString()));

            HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                // act like a browser to force the OSM tile server to respond
                // TODO: remove this transgression of OSM's fair use policy
                .header("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/96.0.4664.110 Safari/537.36")
                .build();

            HttpResponse<InputStream> response = client.send(request, HttpResponse.BodyHandlers.ofInputStream());
            return response.body();
        } catch (URISyntaxException use) {
            System.out.println(use.getMessage());
        } catch (InterruptedException ie) {
            System.out.println(ie.getMessage());
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
        }
        return null;
    }
}
