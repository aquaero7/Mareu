package com.example.mareu.model;

import java.util.List;

/**
 * Model object representing a Meeting Room
 */
public class Room {

    /** Identifier */
    private int mId;

    /** Subject */
    private String mName;

    /** Capacity */
    private int mCapacity;

    /** Color */
    private int mColor;

    /** Reservations */
    private List<ReservationSlot> mReservationSlots;



    public Room(int id, String name, int capacity, int color, List<ReservationSlot> reservationSlots) {
        mId = id;
        mName = name;
        mCapacity = capacity;
        mColor = color;
        mReservationSlots = reservationSlots;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public int getCapacity() {
        return mCapacity;
    }

    public void setCapacity(int capacity) {
        mCapacity = capacity;
    }

    public int getColor() {
        return mColor;
    }

    public void setColor(int color) {
        mColor = color;
    }

    public List<ReservationSlot> getReservationSlots() {
        return mReservationSlots;
    }

    public void setReservationSlots(List<ReservationSlot> reservationSlots) {
        mReservationSlots = reservationSlots;
    }

}
