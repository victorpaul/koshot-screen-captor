package com.sukinsan.shot.util;

import java.awt.image.BufferedImage;
import java.io.File;

public interface CropUtil {

    File crop(int x, int y, int width, int height);

    File save(BufferedImage bufferedImage);
}
