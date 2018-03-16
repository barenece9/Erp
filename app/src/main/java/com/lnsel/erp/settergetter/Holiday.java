package com.lnsel.erp.settergetter;

/**
 * Created by db on 5/4/2017.
 */
public class Holiday {
    public String day="";
    public String month="";
    public String year="";
    public String date="";
    public String holiday_type_name="";

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHoliday_type_name() {
        return holiday_type_name;
    }

    public void setHoliday_type_name(String holiday_type_name) {
        this.holiday_type_name = holiday_type_name;
    }
}
