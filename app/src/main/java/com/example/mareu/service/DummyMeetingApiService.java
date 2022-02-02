package com.example.mareu.service;

import com.example.mareu.model.Meeting;
import com.example.mareu.model.ReservationSlot;
import com.example.mareu.model.Room;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DummyMeetingApiService implements MeetingApiService {

    private List<Meeting> meetings = getDummyMeetings();

    private List<Room> rooms = DummyMeetingGenerator.generateRooms();
    private List<ReservationSlot> reservationSlots = DummyMeetingGenerator.generateReservationSlots();

    @Override
    public List<Meeting> getDummyMeetings() {
        return DummyMeetingGenerator.generateMeetings();
    }

    @Override
    public List<Meeting> getMeetings() {
        return meetings;
    }

    @Override
    public List<Meeting> getMeetingsByPlace(Room room) {
        List<Meeting> meetingsFilteredByPlace = new ArrayList<>();
        for(Meeting meeting : meetings) {
            if(meeting.getRoom() == room) {
                meetingsFilteredByPlace.add(meeting);
            }
        }
        return meetingsFilteredByPlace;
    }

    @Override
    public List<Meeting> getMeetingsByDate(String date) {
        List<Meeting> meetingsFilteredByDate = new ArrayList<>();
        for(Meeting meeting : meetings) {
            if(meeting.getDate().equals(date)) {
                meetingsFilteredByDate.add(meeting);
            }
        }
        return meetingsFilteredByDate;
    }

    @Override
    public void deleteMeeting(Meeting meeting) {
        meetings.remove(meeting);
    }

    @Override
    public void createMeeting(Meeting meeting) {
        meetings.add(meeting);
    }

    @Override
    public List<Room> getRooms() {
        return rooms;
    }

    @Override
    public Room getRoomById(int id) {
        for(Room room : rooms) {
            if(room.getId() == id) {
                return room;
            }
        }
        return null;
    }

    @Override
    public Room getRoomByName(String name) {
        for(Room room : rooms) {
            if(room.getName() == name) {
                return room;
            }
        }
        return null;
    }

    @Override
    public List<ReservationSlot> getReservationSlots() {
        return reservationSlots;
    }
}
