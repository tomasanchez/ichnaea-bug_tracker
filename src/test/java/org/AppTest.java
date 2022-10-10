package org;

import org.ichnaea.core.mvc.view.AppView;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class AppTest {

    AppView app;

    @BeforeEach
    public void setUp() {
        app = new AppView();
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