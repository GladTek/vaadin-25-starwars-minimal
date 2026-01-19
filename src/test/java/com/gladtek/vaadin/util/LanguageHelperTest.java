package com.gladtek.vaadin.util;

import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LanguageHelperTest {

    @Test
    void testIsRtl_Arabic() {
        assertTrue(LanguageHelper.isRtl(Locale.forLanguageTag("ar")));
    }

    @Test
    void testIsRtl_Hebrew() {
        assertTrue(LanguageHelper.isRtl(Locale.forLanguageTag("he")));
    }

    @Test
    void testIsRtl_Persian() {
        assertTrue(LanguageHelper.isRtl(Locale.forLanguageTag("fa")));
    }

    @Test
    void testIsRtl_Urdu() {
        assertTrue(LanguageHelper.isRtl(Locale.forLanguageTag("ur")));
    }

    @Test
    void testIsRtl_English() {
        assertFalse(LanguageHelper.isRtl(Locale.ENGLISH));
    }

    @Test
    void testIsRtl_French() {
        assertFalse(LanguageHelper.isRtl(Locale.FRENCH));
    }

    @Test
    void testIsRtl_German() {
        assertFalse(LanguageHelper.isRtl(Locale.GERMAN));
    }

    @Test
    void testIsRtl_Null() {
        assertFalse(LanguageHelper.isRtl(null));
    }

    @Test
    void testGetRtlLocales_NotEmpty() {
        assertFalse(LanguageHelper.getRtlLocales().isEmpty());
    }

    @Test
    void testGetRtlLocales_ContainsArabic() {
        assertTrue(LanguageHelper.getRtlLocales().stream()
                .anyMatch(locale -> locale.getLanguage().equals("ar")));
    }
}
