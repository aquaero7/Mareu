package com.example.mareu.service;

import com.example.mareu.model.Meeting;
import com.example.mareu.model.ReservationSlot;
import com.example.mareu.model.Room;

import java.util.Date;
import java.util.List;

/**
 * Meeting API client
 */
public interface MeetingApiService {

    /* Methods about Meeting */

    /**
     * Get all my Meetings
     * @return {@link List}
     */
    List<Meeting> getMeetings();

    /**
     * Get Meetings by their place
     * @return {@link List}
     */
    List<Meeting> getMeetingsByPlace(Room room);

    /**
     * Get Meeting by its date
     * @return {@link List}
     */
    List<Meeting> getMeetingsByDate(String date);

    /**
     * Deletes a Meeting
     * @param meeting
     */
    void deleteMeeting(Meeting meeting);

    /**
     * Create a Meeting
     * @param meeting
     */
    void createMeeting(Meeting meeting);


    /* Methods about Rooms */

    /**
     * Get all Rooms
     * @return {@link List}
     */
    List<Room> getRooms();

    /**
     * Get Room by its Id
     * @return room
     */
    Room getRoomById(int id);

    /**
     * Get Room by its name
     * @return room
     */
    Room getRoomByName(String name);


    /* Methods about ReservationSlots for a room */

    /**
     * Get all ReservationSlots
     * @return {@link List}
     */
    List<ReservationSlot> getReservationSlots();

}

