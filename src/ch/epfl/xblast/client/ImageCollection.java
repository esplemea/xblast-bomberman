package ch.epfl.xblast.client;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import javax.imageio.ImageIO;

public final class ImageCollection {
    private final Map<Integer, Image> collection;

    /**
     * Create an ImageCollection from a repertoire
     * 
     * @param repertory
     * @throws URISyntaxException
     * @throws IOException
     * @throws NumberFormatException
     */
    public ImageCollection(String repertoire) {
        Map<Integer, Image> output = new HashMap<>();
        try {
            File dir = new File(ImageCollection.class.getClassLoader()
                    .getResource(repertoire).toURI());
            for (int i = 0; i < dir.listFiles().length; ++i) {
                output.put(
                        Integer.parseInt(
                                dir.listFiles()[i].getName().substring(0, 3)),
                        ImageIO.read(dir.listFiles()[i]));
            }
        } catch (URISyntaxException | NumberFormatException | IOException e) {
        }
        collection = Collections.unmodifiableMap(new HashMap<>(output));
    }

    /**
     * give the image from the actual repertoire in the index position
     * 
     * @param index
     * @return the right image or throws a NoSuchElementException if there's no
     *         Image in the index
     */
    public Image image(int index) {
        if (collection.containsKey(index))
            return collection.get(index);
            
        throw new NoSuchElementException();
    }

    /**
     * give the image from the actual repertoire in the index position
     * 
     * @param index
     * @return the right image or null if there's no Image in the index
     */
    public Image imageOrNull(int index) {
        if (collection.containsKey(index))
            return collection.get(index);
        return null;
    }
}
