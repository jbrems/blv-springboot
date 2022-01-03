package net.blaasveld.springboot.tiles;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.stream.Stream;

@Service
public class TileCacheService {
    private static final String CACHE_DIR = "src/main/resources/tileCache";

    private static final Logger LOGGER = LoggerFactory.getLogger(TileCacheService.class);

    private String getFileName(String z, String x, String y) {
        return String.format("%s/%s-%s-%s.png", CACHE_DIR, z, x, y);
    }

    private File[] getFiles() {
        return new File(CACHE_DIR).listFiles();
    }

    public boolean hasTile(String z, String x, String y) {
        LOGGER.debug(String.format("Searching cache for tile %s %s %s", z, x, y));
        return new File(this.getFileName(z, x, y)).isFile();
    }

    public void cacheTile(String z, String x, String y, InputStream tileStream) {
        try {
            LOGGER.debug(String.format("Caching tile %s %s %s", z, x, y));

            File file = new File(this.getFileName(z, x, y));

            FileUtils.copyInputStreamToFile(tileStream, file);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }

    public String[] getTiles() {
        LOGGER.debug("Listing all tiles in cache");
        return Stream.of(this.getFiles()).map(File::getName).toArray(String[]::new);
    }

    public InputStream getTile(String z, String x, String y) {
        try {
            LOGGER.debug(String.format("Reading tile %s %s %s from cache", z, x, y));
            return new FileInputStream(this.getFileName(z, x, y));
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return null;
        }
    }

    public void deleteTiles() {
        LOGGER.debug("Deleting all tiles from cache");
        Stream.of(this.getFiles()).forEach(File::delete);
    }

    public void deleteTile(String z, String x, String y) {
        LOGGER.debug(String.format("Deleting tile %s %s %s from cache", z, x, y));
        File tile = new File(this.getFileName(z, x, y));
        tile.delete();
    }
}
