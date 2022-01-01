package net.blaasveld.springboot.tiles;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

@Service
public class TileCacheService {
    private static final String CACHE_DIR = "src/main/resources/tileCache";

    private static final Logger LOGGER = LoggerFactory.getLogger(TileCacheService.class);

    private String getFileName(int z, int x, int y) {
        return String.format("%s/%d-%d-%d.png", CACHE_DIR, z, x, y);
    }

    public boolean hasTile(int z, int x, int y) {
        LOGGER.debug(String.format("Searching cache for tile %d %d %d", z, x, y));
        return new File(this.getFileName(z, x, y)).isFile();
    }

    public void cacheTile(int z, int x, int y, InputStream tileStream) {
        try {
            LOGGER.debug(String.format("Caching tile %d %d %d", z, x, y));

            File file = new File(this.getFileName(z, x, y));

            FileUtils.copyInputStreamToFile(tileStream, file);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }

    public InputStream getTile(int z, int x, int y) {
        try {
            LOGGER.debug(String.format("Returning tile %d %d %d from cache", z, x, y));
            return new FileInputStream(this.getFileName(z, x, y));
        } catch (FileNotFoundException e) {
            LOGGER.error(e.getMessage());
            return null;
        }
    }
}
