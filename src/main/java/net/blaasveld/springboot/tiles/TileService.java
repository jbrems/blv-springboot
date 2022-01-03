package net.blaasveld.springboot.tiles;

import org.apache.commons.io.input.CloseShieldInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class TileService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TileService.class);

    private TileCacheService tileCacheService;

    @Autowired
    public TileService(TileCacheService tileCacheService) {
        LOGGER.debug("Creating TileService instance");
        this.tileCacheService = tileCacheService;
    }

    public InputStream getTile(String z, String x, String y) {
        LOGGER.debug(String.format("Getting tile %s %s %s", z, x, y));

        if (!this.tileCacheService.hasTile(z, x, y)) {
            LOGGER.debug("Cache miss");
            InputStream tile = this.fetchTile(z, x, y);
            this.tileCacheService.cacheTile(z, x, y, new CloseShieldInputStream(tile));
        }

        return this.tileCacheService.getTile(z, x, y);
    }

    private InputStream fetchTile(String z, String x, String y) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            URI uri = new URI(String.format("https://tile.openstreetmap.org/%s/%s/%s.png", z, x, y));

            LOGGER.debug(String.format("Fetching tile from %s", uri.toString()));

            HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                // act like a browser to force the OSM tile server to respond
                // TODO: remove this transgression of OSM's fair use policy
                .header("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/96.0.4664.110 Safari/537.36")
                .build();

            HttpResponse<InputStream> response = client.send(request, HttpResponse.BodyHandlers.ofInputStream());
            return response.body();
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return null;
        }
    }
}
