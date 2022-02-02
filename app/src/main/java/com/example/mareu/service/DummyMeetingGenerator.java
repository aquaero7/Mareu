package com.example.mareu.service;

import android.os.Parcelable;

import com.example.mareu.R;
import com.example.mareu.model.Meeting;
import com.example.mareu.model.ReservationSlot;
import com.example.mareu.model.Room;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public abstract class DummyMeetingGenerator implements Parcelable {

    public static List<Room> ROOMS = Arrays.asList(
            new Room(1, "Azur", 7, R.color.azur, Arrays.asList(new ReservationSlot(1, "15/02/2022", "10:00", "10:45"), new ReservationSlot(1, "23/02/2022", "15:00", "15:45"))),
            new Room(2, "Magma", 7, R.color.magma, Arrays.asList(new ReservationSlot(1, "11/02/2022", "09:00", "09:45"), new ReservationSlot(1, "07/02/2022", "14:00", "14:45"))),
            new Room(3, "Amazone", 7, R.color.amazone, Arrays.asList(new ReservationSlot(1, "03/02/2022", "11:30", "12:15"), new ReservationSlot(1, "15/02/2022", "16:45", "17:30"))),
            new Room(4, "Zénith", 7, R.color.zenith, Arrays.asList(new ReservationSlot(1, "23/02/2022", "09:15", "10:00"), new ReservationSlot(1, "11/02/2022", "14:30", "15:15"))),
            new Room(5, "Aurore", 7, R.color.aurore, Arrays.asList(new ReservationSlot(1, "07/02/2022", "11:00", "11:45"), new ReservationSlot(1, "11/02/2022", "13:30", "14:15"))),
            new Room(6, "Corail", 7, R.color.corail, Arrays.asList(new ReservationSlot(1, "07/02/2022", "10:00", "10:45"), new ReservationSlot(1, "23/02/2022", "15:00", "15:45"))),
            new Room(7, "Ecorce", 7, R.color.ecorce, Arrays.asList(new ReservationSlot(1, "11/02/2022", "09:00", "09:45"), new ReservationSlot(1, "03/02/2022", "14:00", "14:45"))),
            new Room(8, "Orage", 7, R.color.orage, Arrays.asList(new ReservationSlot(1, "11/02/2022", "11:30", "12:15"), new ReservationSlot(1, "23/02/2022", "16:45", "17:30"))),
            new Room(9, "Dune", 7, R.color.dune, Arrays.asList(new ReservationSlot(1, "15/02/2022", "09:15", "10:00"), new ReservationSlot(1, "07/02/2022", "14:30", "15:15"))),
            new Room(10, "Cosmos", 7, R.color.cosmos, Arrays.asList(new ReservationSlot(1, "23/02/2022", "11:00", "11:45"), new ReservationSlot(1, "03/02/2022", "13:30", "14:15")))
    );

    public static List<Meeting> DUMMY_MEETINGS = Arrays.asList(
            new Meeting(1, "Réunion A", "15/02/2022", "10:00", "10:45", ROOMS.get(0), "alexandra@lamzone.com, francis@lamzone.com, maxime@lamzone.com, patrick@lamzone.com"),
            new Meeting(2, "Réunion B", "23/02/2022", "15:00", "15:45", ROOMS.get(0), "alexandra@lamzone.com, francis@lamzone.com, maxime@lamzone.com, patrick@lamzone.com"),
            new Meeting(3, "Réunion C", "11/02/2022", "09:00", "09:45", ROOMS.get(1), "francis@lamzone.com, alexandra@lamzone.com, maxime@lamzone.com, patrick@lamzone.com"),
            new Meeting(4, "Réunion D", "07/02/2022", "14:00", "14:45", ROOMS.get(1), "francis@lamzone.com, alexandra@lamzone.com, maxime@lamzone.com, patrick@lamzone.com"),
            new Meeting(5, "Réunion E", "03/02/2022", "11:30", "12:15", ROOMS.get(2), "alexandra@lamzone.com, francis@lamzone.com, maxime@lamzone.com"),
            new Meeting(6, "Réunion F", "15/02/2022", "16:45", "17:30", ROOMS.get(2), "alexandra@lamzone.com, francis@lamzone.com, maxime@lamzone.com"),
            new Meeting(7, "Réunion G", "23/02/2022", "09:15", "10:00", ROOMS.get(3), "alexandra@lamzone.com, francis@lamzone.com, patrick@lamzone.com"),
            new Meeting(8, "Réunion H", "11/02/2022", "14:30", "15:15", ROOMS.get(3), "alexandra@lamzone.com, francis@lamzone.com, patrick@lamzone.com"),
            new Meeting(9, "Réunion I", "07/02/2022", "11:00", "11:45", ROOMS.get(4), "alexandra@lamzone.com, francis@lamzone.com, patrick@lamzone.com"),
            new Meeting(10, "Réunion J", "11/02/2022", "13:30", "14:15", ROOMS.get(4), "francis@lamzone.com, maxime@lamzone.com, patrick@lamzone.com"),
            new Meeting(11, "Réunion K", "07/02/2022", "10:00", "10:45", ROOMS.get(5), "francis@lamzone.com, maxime@lamzone.com, patrick@lamzone.com"),
            new Meeting(12, "Réunion L", "23/02/2022", "15:00", "15:45", ROOMS.get(5), "francis@lamzone.com, alexandra@lamzone.com, maxime@lamzone.com"),
            new Meeting(13, "Réunion M", "11/02/2022", "09:00", "09:45", ROOMS.get(6), "francis@lamzone.com, alexandra@lamzone.com, maxime@lamzone.com"),
            new Meeting(14, "Réunion N", "03/02/2022", "14:00", "14:45", ROOMS.get(6), "francis@lamzone.com, alexandra@lamzone.com, patrick@lamzone.com"),
            new Meeting(15, "Réunion O", "11/02/2022", "11:30", "12:15", ROOMS.get(7), "francis@lamzone.com, alexandra@lamzone.com, patrick@lamzone.com"),
            new Meeting(16, "Réunion P", "23/02/2022", "16:45", "17:30", ROOMS.get(7), "francis@lamzone.com, alexandra@lamzone.com, patrick@lamzone.com"),
            new Meeting(17, "Réunion Q", "15/02/2022", "09:15", "10:00", ROOMS.get(8), "francis@lamzone.com, maxime@lamzone.com"),
            new Meeting(18, "Réunion R", "07/02/2022", "14:30", "15:15", ROOMS.get(8), "francis@lamzone.com, maxime@lamzone.com"),
            new Meeting(19, "Réunion S", "23/02/2022", "11:00", "11:45", ROOMS.get(9), "francis@lamzone.com, patrick@lamzone.com"),
            new Meeting(20, "Réunion T", "03/02/2022", "13:30", "14:15", ROOMS.get(9), "francis@lamzone.com, patrick@lamzone.com")
    );



    static List<Room> generateRooms() {
        // return new ArrayList<>(ROOMS);  // Unsorted
        return sortRoomsByName(new ArrayList<>(ROOMS));   // Sorted
    }

    static List<Meeting> generateMeetings() {
        //return new ArrayList<>(DUMMY_MEETINGS);   // Unsorted
        return sortMeetingsByDate(sortMeetingsByStart(sortMeetingsByEnd(new ArrayList<>(DUMMY_MEETINGS))));   // Sorted
    }

    static List<ReservationSlot> generateReservationSlots() {
        // return new ArrayList<>(DUMMY_SLOTS);
        return null;
    }


    static List<Meeting> sortMeetingsByDate(List<Meeting> meetings) {
        Collections.sort(meetings, Meeting.ComparatorDate);
        return meetings;
    }

    static List<Meeting> sortMeetingsByStart(List<Meeting> meetings) {
        Collections.sort(meetings, Meeting.ComparatorStart);
        return meetings;
    }

    static List<Meeting> sortMeetingsByEnd(List<Meeting> meetings) {
        Collections.sort(meetings, Meeting.ComparatorEnd);
        return meetings;
    }

    static List<Room> sortRoomsByName(List<Room> rooms) {
        Collections.sort(rooms, Room.ComparatorName);
        return rooms;
    }
}
