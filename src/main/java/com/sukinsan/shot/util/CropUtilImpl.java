package com.sukinsan.shot.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class CropUtilImpl implements CropUtil {

    @Override
    public File crop(int x, int y, int width, int height) {
        try {
            Robot robot = new Robot();
            BufferedImage bi = robot.createScreenCapture(new Rectangle(x, y, width, height));
            File outputfile = Files.createTempFile("kosh", "shot").toFile();
            if (!outputfile.exists()) {
                outputfile.createNewFile();
            }
            ImageIO.write(bi, "jpg", outputfile);

            return outputfile;
        } catch (AWTException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
