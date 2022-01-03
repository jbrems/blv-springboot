package net.blaasveld.springboot.tiles;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.InputStream;

@RestController
@RequestMapping("/tiles/cache")
public class TileCacheController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TileCacheController.class);

    private TileCacheService tileCacheService;

    public TileCacheController(TileCacheService tileCacheService) {
        LOGGER.debug("Creating TileCacheController instance");
        this.tileCacheService = tileCacheService;
    }

    @GetMapping()
    public String[] getTiles() {
        LOGGER.info("Getting all cached tiles");
        return this.tileCacheService.getTiles();
    }

    @GetMapping(value = "/{z}/{x}/{y}", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<InputStreamResource> getTile(@PathVariable String z, @PathVariable String x, @PathVariable String y) {
        LOGGER.info(String.format("Serving tile %s %s %s from cache", z, x, y));

        if (!tileCacheService.hasTile(z, x, y)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        InputStream tile = this.tileCacheService.getTile(z, x, y);

        InputStreamResource tileResource = new InputStreamResource(tile);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(tileResource);
    }

    @DeleteMapping()
    public void deleteTiles() {
        LOGGER.info("Deleting all tiles from cache");
        this.tileCacheService.deleteTiles();
    }

    @DeleteMapping("/{z}/{x}/{y}")
    public void deleteTile(@PathVariable String z, @PathVariable String x, @PathVariable String y) {
        LOGGER.info(String.format("Deleting tile %s %s %s", z, x, y));
        this.tileCacheService.deleteTile(z, x, y);
    }
}
