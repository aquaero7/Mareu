package com.example.mareu.model;


/**
 * Model object representing a Reservation Slot
 */
public class ReservationSlot {

    /** Identifier */
    private long mId;

    /** Date */
    private String mDate;

    /** Start time */
    private String mStart;

    /** End time */
    private String mEnd;

    public ReservationSlot(long id, String date, String start, String end) {
        mId = id;
        mDate = date;
        mStart = start;
        mEnd = end;
    }

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        mDate = date;
    }

    public String getStart() {
        return mStart;
    }

    public void setStart(String start) {
        mStart = start;
    }

    public String getEnd() {
        return mEnd;
    }

    public void setEnd(String end) {
        mEnd = end;
    }
}
