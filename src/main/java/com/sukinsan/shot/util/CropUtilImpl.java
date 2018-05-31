package com.sukinsan.shot.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class CropUtilImpl implements CropUtil {

    @Override
    public File save(BufferedImage bi) {
        try {
            String fileanme = System.currentTimeMillis() + "_" + Files.createTempFile("kosh", "shot").toFile().getName() + ".jpg";
            File outputfile = new File(getUploadFolder(), fileanme);

            if (!outputfile.exists()) {
                outputfile.createNewFile();
            }
            ImageIO.write(bi, "jpg", outputfile);
            return outputfile;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public File crop(int x, int y, int width, int height) {
        try {
            Robot robot = new Robot();
            BufferedImage bi = robot.createScreenCapture(new Rectangle(x, y, width, height));
            return save(bi);
        } catch (AWTException e) {
            e.printStackTrace();
        }
        return null;
    }

    private File getUploadFolder() {
        File uploadFOlder = new File(System.getProperty("user.home"), "koshot/myUploads");
        if (!uploadFOlder.exists()) {
            uploadFOlder.mkdirs();
        }
        return uploadFOlder;
    }

}
