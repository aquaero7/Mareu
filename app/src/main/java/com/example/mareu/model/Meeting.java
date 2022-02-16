package com.example.mareu.model;

import androidx.annotation.NonNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
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
    private List<String> mParticipants;


    public Meeting(long id, String name, String date, String start, String end, Room room, List<String> participants) {
        mId = id;
        mName = name;
        mDate = date;
        mStart = start;
        mEnd = end;
        mRoom = room;
        mParticipants = new ArrayList<>(participants);
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

    public List<String> getParticipants() {
        return mParticipants;
    }

    public void setParticipants(List<String> participants) {
        mParticipants = participants;
    }

    public String getParticipantsToString() {
        String sParticipants = "";
        for (String participant : mParticipants) {
            sParticipants = sParticipants + participant + ", ";
        }
        return sParticipants;
    }

    // Création d'un timestamp
    @NonNull
    @Override
    public String toString() {
        // return super.toString();
        return mDate + " " + mStart + " " + mEnd;
    }

    // Création des comparateurs pour les tris

    public static Comparator<Meeting> ComparatorDate = new Comparator<Meeting>() {
        @Override
        public int compare(@NonNull Meeting o1, @NonNull Meeting o2) {
            SimpleDateFormat dfDate = new SimpleDateFormat("dd/MM/yyyy");
            try {
                // return dfDate.parse(o1.getDate()).compareTo(dfDate.parse(o2.getDate()));    // Tri croissant
                return dfDate.parse(o2.getDate()).compareTo(dfDate.parse(o1.getDate()));    // Tri décroissant
            } catch (ParseException e) {
                e.printStackTrace();
                System.out.println("ParseException dans " + getClass() + " / " + this + " : " + e.getMessage());
                return 0;
            }
        }
    };

    public static Comparator<Meeting> ComparatorStart = new Comparator<Meeting>() {
        @Override
        public int compare(@NonNull Meeting o1, @NonNull Meeting o2) {
            return o1.getStart().compareTo(o2.getStart());    // Tri croissant
            // return o2.getStart().compareTo(o1.getStart());    // Tri décroissant
        }
    };

    public static Comparator<Meeting> ComparatorEnd = new Comparator<Meeting>() {
        @Override
        public int compare(@NonNull Meeting o1, @NonNull Meeting o2) {
            return o1.getEnd().compareTo(o2.getEnd());    // Tri croissant
            // return o2.getEnd().compareTo(o1.getEnd());    // Tri décroissant
        }
    };

}
