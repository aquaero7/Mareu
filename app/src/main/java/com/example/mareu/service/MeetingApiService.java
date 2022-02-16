package com.example.mareu.service;

import com.example.mareu.model.Meeting;
import com.example.mareu.model.Room;

import java.util.List;

/**
 * Meeting API client
 */
public interface MeetingApiService {

    /* Methods about Meeting */

    // Get methods

    /**
     * Get all my Meetings
     * @return {@link List}
     */
    List<Meeting> getDummyMeetings();

    /**
     * Get all my Rooms
     * @return {@link List}
     */
    public List<Room> getDummyRooms();

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

    /**
     * Get list of Room names
     * @return @String
     */
    CharSequence[] getRoomsList();


    // Creation ans deletion methods

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


    /* Methods about ReservationSlots for a room */

    /**
     * Release a reservation slot when deleting meeting
     * @param meeting
     */
    public void deleteRoomReservationSlot(Meeting meeting);

    /**
     * Create a reservation slot when creating meeting
     * @param meeting
     */
    public void createRoomReservationSlot(Meeting meeting);


    // Sort methods

    /**
     * Sort meetings list by date
     * @param meetings
     */
    public List<Meeting> sortMeetingsByDate(List<Meeting> meetings);

    /**
     * Sort meetings list by start time
     * @param meetings
     */
    public List<Meeting> sortMeetingsByStart(List<Meeting> meetings);

    /**
     * Sort meetings list by end time
     * @param meetings
     */
    public List<Meeting> sortMeetingsByEnd(List<Meeting> meetings);

    /**
     * Sort rooms list by start name
     * @param rooms
     */
    public List<Room> sortRoomsByName(List<Room> rooms);

}

