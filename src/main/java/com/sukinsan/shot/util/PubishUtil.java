package com.sukinsan.shot.util;

import java.io.File;

public interface PubishUtil {
    interface OnPubish {
        void success(String res);

        void fail(String res);
    }

    void publish(String auth, File file, OnPubish onPubish);
}
