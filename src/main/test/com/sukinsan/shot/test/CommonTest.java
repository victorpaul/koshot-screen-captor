package com.sukinsan.shot.test;

import com.sukinsan.shot.util.SettingsUtil;
import com.sukinsan.shot.util.SettingsUtilImpl;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CommonTest {

    @Test
    public void check_base_auth() {
        SettingsUtil set = new SettingsUtilImpl();
        assertEquals("Basic dXNlcm5hbWU6cXdlcnR5", set.baseAuth("username", "qwerty"));
    }
}
