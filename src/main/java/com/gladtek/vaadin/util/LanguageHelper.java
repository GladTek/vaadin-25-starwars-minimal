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
     * Returns the appropriate locale for number and date formatting.
     * For Arabic, it uses 'ar-u-nu-latn' to explicitly request Western Arabic numerals (1, 2, 3),
     * which are standard in many modern contexts and the Maghreb region.
     *
     * @param locale The base locale.
     * @return The formatting locale.
     */
    public static Locale getFormattingLocale(Locale locale) {
        if (locale != null && locale.getLanguage().equals("ar")) {
            // Force Latin numeral system (Western Arabic numerals)
            return Locale.forLanguageTag("ar-u-nu-latn");
        }
        return locale;
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
