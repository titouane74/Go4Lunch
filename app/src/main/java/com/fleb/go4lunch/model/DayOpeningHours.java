package com.fleb.go4lunch.model;

/**
 * Created by Florence LE BOURNOT on 15/10/2020
 */
public class DayOpeningHours {

    private int dayNumber;
    private int dayNumService;
    private int dayOpenHour;
    private int dayCloseHour;
    private boolean dayIsOpen;
    private String dayDescription;

    public DayOpeningHours() {}

    public DayOpeningHours(int pDayNumber, int pDayNumService, int pDayOpenHour, int pDayCloseHour, boolean pDayIsOpen, String pDayDescription) {
        dayNumber = pDayNumber;
        dayNumService = pDayNumService;
        dayOpenHour = pDayOpenHour;
        dayCloseHour = pDayCloseHour;
        dayIsOpen = pDayIsOpen;
        dayDescription = pDayDescription;
    }

    public int getDayNumber() {
        return dayNumber;
    }

    public void setDayNumber(int pDayNumber) {
        dayNumber = pDayNumber;
    }

    public int getDayNumService() {
        return dayNumService;
    }

    public void setDayNumService(int pDayNumService) {
        dayNumService = pDayNumService;
    }

    public int getDayOpenHour() {
        return dayOpenHour;
    }

    public void setDayOpenHour(int pDayOpenHour) {
        dayOpenHour = pDayOpenHour;
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
}
