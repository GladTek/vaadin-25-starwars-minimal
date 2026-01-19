package com.gladtek.vaadin.utils;

import com.vaadin.flow.component.datepicker.DatePicker;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;


public class DatePickerI18nUtil {

    public static DatePicker.DatePickerI18n getI18n(Locale locale) {
        // Apply Arab Western locale for months values
        if ("ar".equals(locale.getLanguage())) {
            locale = Locale.of("ar", "TN");
        }

        DateFormatSymbols symbols = new DateFormatSymbols(locale);
        DatePicker.DatePickerI18n i18n = new DatePicker.DatePickerI18n();
        i18n.setDateFormat("yyyy-MM-dd");

        String[] months = symbols.getMonths();
        i18n.setMonthNames(Arrays.asList(Arrays.copyOf(months, 12)));

        String[] weekdays = symbols.getWeekdays();
        List<String> weekDaysList = new ArrayList<>();
        weekDaysList.addAll(Arrays.asList(weekdays).subList(1, 8));
        i18n.setWeekdays(weekDaysList);

        String[] shortWeekdays = symbols.getShortWeekdays();
        List<String> shortWeekDaysList = new ArrayList<>();
        shortWeekDaysList.addAll(Arrays.asList(shortWeekdays).subList(1, 8));
        i18n.setWeekdaysShort(shortWeekDaysList);

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("vaadin-i18n.translations", locale);
        i18n.setToday(bundle.getString("components.datepicker.today"));
        i18n.setCancel(bundle.getString("components.datepicker.cancel"));

        return i18n;
    }
}
