package com.gladtek.vaadin.services;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class UserSessionTest {

    private UserSession userSession;

    @BeforeEach
    void setUp() {
        userSession = new UserSession();
    }

    @Test
    void testSelectedSide_InitiallyNull() {
        assertNull(userSession.getSelectedSide());
    }

    @Test
    void testSetSelectedSide_Dark() {
        userSession.setSelectedSide("dark");
        assertEquals("dark", userSession.getSelectedSide());
    }

    @Test
    void testSetSelectedSide_Light() {
        userSession.setSelectedSide("light");
        assertEquals("light", userSession.getSelectedSide());
    }


}
