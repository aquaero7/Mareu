package com.example.mareu.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

/**
 * Model object representing a Meeting
 */
public class Meeting {

    /** Identifier */
    private long mId;

    /** Subject */
    private String mName;

    /** Date */
    private String mDate;

    /** Start time */
    private String mStart;

    /** End time */
    private String mEnd;

    /** Room */
    private Room mRoom;

    /** Participants */
    private String mParticipants;


    public Meeting(long id, String name, String date, String start, String end, Room room, String participants) {
        mId = id;
        mName = name;
        mDate = date;
        mStart = start;
        mEnd = end;
        mRoom = room;
        mParticipants = participants;
    }

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
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

    public Room getRoom() {
        return mRoom;
    }

    public void setRoom(Room room) {
        mRoom = room;
    }

    public String getParticipants() {
        return mParticipants;
    }

    public void setParticipants(String participants) {
        mParticipants = participants;
    }
}
