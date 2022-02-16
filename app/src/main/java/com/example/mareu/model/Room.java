package com.example.mareu.model;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Comparator;
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
        mReservationSlots = new ArrayList<>(reservationSlots);
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

    public void removeReservationSlot(ReservationSlot reservationSlot) {
        mReservationSlots.remove(reservationSlot);
    }

    public void addReservationSlot(ReservationSlot reservationSlot) {
        mReservationSlots.add(reservationSlot);
    }

    // Renvoie les informations de la salle
    @NonNull
    @Override
    public String toString() {
        // return super.toString();
        return mName + " (capacité : " + mCapacity + " personnes)";
    }

    // Création du comparateur pour le tri
    public static Comparator<Room> ComparatorName = new Comparator<Room>() {
        @Override
        public int compare(@NonNull Room o1, @NonNull Room o2) {
            return o1.getName().compareTo(o2.getName());    // Tri croissant
            // return o2.getName().compareTo(o1.getName());    // Tri décroissant
        }
    };

}
