/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package res;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import javax.imageio.ImageIO;

/**
 * Helps load resources like files and images.
 */
public class ResourceLoader {

    /**
     * Loads a resource file as an InputStream.
     * 
     * @param resName The name of the resource file.
     * @return The InputStream for the resource, or null if not found.
     */
    public static InputStream loadResource(String resName) {
        return ResourceLoader.class.getClassLoader().getResourceAsStream(resName);
    }

    /**
     * Loads an image resource.
     * 
     * @param resName The name of the image file.
     * @return The loaded Image.
     * @throws IOException If the image cannot be loaded.
     */
    public static Image loadImage(String resName) throws IOException {
        URL url = ResourceLoader.class.getClassLoader().getResource(resName);
        return ImageIO.read(url);
    }
}

