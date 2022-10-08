package org;

import org.ichnaea.view.MainFrame;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class AppTest {

    MainFrame app;

    @BeforeEach
    public void setUp() {
        app = new MainFrame();
    }

    @AfterEach
    public void tearDown() {
        app.dispose();
    }

    @Test
    public void shouldOpenApplication() {
        app.launch();
        assertTrue(app.isVisible());
    }
}