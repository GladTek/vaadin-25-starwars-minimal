package com.gladtek.vaadin.util;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class LanguageHelper {

    private static final List<String> RTL_LANGUAGES = Arrays.asList(
            "ar", // Arabic
            "arc", // Aramaic
            "dv", // Divehi
            "fa", // Persian
            "ha", // Hausa
            "he", // Hebrew
            "iw", // Hebrew (old code)
            "khw", // Khowar
            "ks", // Kashmiri
            "ku", // Kurdish
            "ps", // Pashto
            "ur", // Urdu
            "yi" // Yiddish
    );

    /**
     * Checks if the given locale is a Right-To-Left language.
     *
     * @param locale The locale to check.
     * @return true if the language is typically written RTL.
     */
    public static boolean isRtl(Locale locale) {
        if (locale == null) {
            return false;
        }
        return RTL_LANGUAGES.contains(locale.getLanguage());
    }

    /**
     * Returns a list of common RTL Locale objects.
     *
     * @return List of RTL Locales.
     */
    public static List<Locale> getRtlLocales() {
        return RTL_LANGUAGES.stream().map(Locale::forLanguageTag).toList();
    }
}
