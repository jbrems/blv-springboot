package net.blaasveld.springboot.tiles;

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

    @GetMapping(value = "/{z}/{x}/{y}", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<InputStreamResource> getTile(@PathVariable String z, @PathVariable String x, @PathVariable String y) {
        InputStream tileStream = TileService.getTile(Integer.parseInt(z), Integer.parseInt(x), Integer.parseInt(y));

        InputStreamResource tileResource = new InputStreamResource(tileStream);

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(tileResource);
    }
}
