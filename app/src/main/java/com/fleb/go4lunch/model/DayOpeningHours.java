package com.fleb.go4lunch.model;

/**
 * Created by Florence LE BOURNOT on 15/10/2020
 */
public class DayOpeningHours {

    private int dayCurrentOpenDay;
    private int dayCase;
    private int dayNextOpenHour;
    private int dayCloseHour;
    private boolean dayIsOpen;
    private String dayDescription;
    private int dayNextOpenDay;
    private int dayLastCloseHour;

    public DayOpeningHours() {}

    public DayOpeningHours(int pDayCurrentOpenDay, int pDayCase, int pDayNextOpenHour, int pDayCloseHour,
                           boolean pDayIsOpen, String pDayDescription, int pDayNextOpenDay, int pDayLastCloseHour) {
        dayCurrentOpenDay = pDayCurrentOpenDay;
        dayCase = pDayCase;
        dayNextOpenHour = pDayNextOpenHour;
        dayCloseHour = pDayCloseHour;
        dayIsOpen = pDayIsOpen;
        dayDescription = pDayDescription;
        dayNextOpenDay = pDayNextOpenDay;
        dayLastCloseHour = pDayLastCloseHour;
    }

    public int getDayCurrentOpenDay() {
        return dayCurrentOpenDay;
    }

    public void setDayCurrentOpenDay(int pDayCurrentOpenDay) {
        dayCurrentOpenDay = pDayCurrentOpenDay;
    }

    public int getDayCase() {
        return dayCase;
    }

    public void setDayCase(int pDayCase) {
        dayCase = pDayCase;
    }

    public int getDayNextOpenHour() {
        return dayNextOpenHour;
    }

    public void setDayNextOpenHour(int pDayNextOpenHour) {
        dayNextOpenHour = pDayNextOpenHour;
    }

    public int getDayCloseHour() {
        return dayCloseHour;
    }

    public void setDayCloseHour(int pDayCloseHour) {
        dayCloseHour = pDayCloseHour;
    }

    public boolean isDayIsOpen() {
        return dayIsOpen;
    }

    public void setDayIsOpen(boolean pDayIsOpen) {
        dayIsOpen = pDayIsOpen;
    }

    public String getDayDescription() {
        return dayDescription;
    }

    public void setDayDescription(String pDayDescription) {
        dayDescription = pDayDescription;
    }

    public int getDayNextOpenDay() {
        return dayNextOpenDay;
    }

    public void setDayNextOpenDay(int pDayNextOpenDay) {
        dayNextOpenDay = pDayNextOpenDay;
    }

    public int getDayLastCloseHour() {
        return dayLastCloseHour;
    }

    public void setDayLastCloseHour(int pDayLastCloseHour) {
        dayLastCloseHour = pDayLastCloseHour;
    }
}
