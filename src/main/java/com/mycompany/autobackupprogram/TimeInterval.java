package com.mycompany.autobackupprogram;

public class TimeInterval {
    private int days;
    private int hours;
    private int minutes;
    
    public TimeInterval(int days, int hours, int minutes) {
        if (days < 0) throw new IllegalArgumentException("Days cannot be negative");
        if (hours < 0 || hours > 23) throw new IllegalArgumentException("Hours must be between 0 and 23");
        if (minutes < 0 || minutes > 59) throw new IllegalArgumentException("Minutes must be between 0 and 59");
        
        this.days = days;
        this.hours = hours;
        this.minutes = minutes;
    }
    
    @Override
    public String toString() {
        return days + "." + hours + ":" + minutes;
    }
    
    public static TimeInterval getTimeIntervalFromString(String time) {
        if (time == null || !time.matches("\\d+\\.\\d{1,2}:\\d{1,2}")) {
            throw new IllegalArgumentException("Invalid time format. Expected format: days.hours:minutes (e.g., 1.12:30)");
        }

        String[] dayAndTime = time.split("\\.");
        int parsedDays = Integer.parseInt(dayAndTime[0]);

        String[] hourAndMinute = dayAndTime[1].split(":");
        int parsedHours = Integer.parseInt(hourAndMinute[0]);
        int parsedMinutes = Integer.parseInt(hourAndMinute[1]);

        if (parsedDays < 0) {
            throw new IllegalArgumentException("Days cannot be negative");
        }
        if (parsedHours < 0 || parsedHours > 23) {
            throw new IllegalArgumentException("Hours must be between 0 and 23");
        }
        if (parsedMinutes < 0 || parsedMinutes > 59) {
            throw new IllegalArgumentException("Minutes must be between 0 and 59");
        }
        
        return new TimeInterval(parsedDays, parsedHours, parsedMinutes);
    }

    public int getDays() {
        return days;
    }

    public int getHours() {
        return hours;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setDays(int days) {
        if (days < 0) throw new IllegalArgumentException("Days cannot be negative");
        this.days = days;
    }

    public void setHours(int hours) {
        if (hours < 0 || hours > 23) throw new IllegalArgumentException("Hours must be between 0 and 23");
        this.hours = hours;
    }

    public void setMinutes(int minutes) {
        if (minutes < 0 || minutes > 59) throw new IllegalArgumentException("Minutes must be between 0 and 59");
        this.minutes = minutes;
    }
    
}
