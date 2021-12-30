package net.blaasveld.springboot.tiles;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class TileService {

    public static InputStream getTile(int z, int x, int y) {
        System.out.println(String.format("Getting tile %d %d %d", z, x, y));
        return TileService.fetchTile(z, x, y);
    }

    private static InputStream fetchTile(int z, int x, int y) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            URI uri = new URI(String.format("https://tile.openstreetmap.org/%d/%d/%d.png", z, x, y));

            System.out.println(String.format("Fetching tile from %s", uri.toString()));

            HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
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
