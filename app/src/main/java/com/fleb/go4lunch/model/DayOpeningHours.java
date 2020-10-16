package com.fleb.go4lunch.model;

import java.util.List;

/**
 * Created by Florence LE BOURNOT on 15/10/2020
 */
public class DayOpeningHours {

    private int dayNumber;
    private int dayNumService;
    private DayService dayService;
    private int dayOpenHour;
    private int dayCloseHour;
    private boolean dayIsOpen;
    private String dayDescription;

    public DayOpeningHours() {}

    public DayOpeningHours(int pDayNumber, int pDayNumService,DayService pDayService, int pDayOpenHour, int pDayCloseHour, boolean pDayIsOpen, String pDayDescription) {
        dayNumber = pDayNumber;
        dayNumService = pDayNumService;
        dayService = pDayService;
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

    public DayService getDayService() {
        return dayService;
    }

    public void setPeriods(DayService pDayService) {
        dayService = pDayService;
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

    public static class DayService {

        private int numService;
        private int closeTime;
        private int openTime;
        private int dayService;

        public DayService() {}

        public DayService(int pNumService, int pCloseTime, int pOpenTime, int pDayService) {
            numService = pNumService;
            closeTime = pCloseTime;
            openTime = pOpenTime;
            dayService = pDayService;
        }

        public int getNumService() {
            return numService;
        }

        public void setNumService(int pNumService) {
            numService = pNumService;
        }

        public int getCloseTime() {
            return closeTime;
        }

        public void setCloseTime(int pCloseTime) {
            closeTime = pCloseTime;
        }

        public int getOpenTime() {
            return openTime;
        }

        public void setOpenTime(int pOpenTime) {
            openTime = pOpenTime;
        }

        public int getDayService() {
            return dayService;
        }

        public void setDayService(int pDayService) {
            dayService = pDayService;
        }

    }

}
