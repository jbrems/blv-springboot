package net.blaasveld.springboot.tiles;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.InputStream;

@RestController
@RequestMapping("/tiles")
public class TileController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TileController.class);

    private TileService tileService;

    @Autowired
    public TileController(TileService tileService) {
        LOGGER.debug("Creating TileController instance");
        this.tileService = tileService;
    }

    @GetMapping(value = "/{z}/{x}/{y}", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<InputStreamResource> getTile(@PathVariable String z, @PathVariable String x, @PathVariable String y) {
        LOGGER.info(String.format("Handling GET request for /tiles/%s/%s/%s", z, x, y));

        InputStream tileStream = tileService.getTile(z, x, y);

        InputStreamResource tileResource = new InputStreamResource(tileStream);

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(tileResource);
    }
}
