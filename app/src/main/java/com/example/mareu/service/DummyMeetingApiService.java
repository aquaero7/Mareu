package com.example.mareu.service;

import com.example.mareu.model.Meeting;
import com.example.mareu.model.ReservationSlot;
import com.example.mareu.model.Room;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DummyMeetingApiService implements MeetingApiService {

    private List<Meeting> meetings = getDummyMeetings();
    private List<Room> rooms = getDummyRooms();

    @Override
    public List<Meeting> getDummyMeetings() {
        // return DummyMeetingGenerator.generateMeetings(); // Unsorted
        return sortMeetingsByDate(sortMeetingsByStart(sortMeetingsByEnd(DummyMeetingGenerator.generateMeetings())));   // Sorted
    }

    @Override
    public List<Room> getDummyRooms() {
        // return DummyMeetingGenerator.generateRooms();   // Unsorted
        return sortRoomsByName(DummyMeetingGenerator.generateRooms());  // Sorted
    }

    @Override
    public List<Meeting> getMeetings() {
        // return meetings; // Unsorted
        return sortMeetingsByDate(sortMeetingsByStart(sortMeetingsByEnd(meetings)));   // Sorted
    }

    @Override
    public List<Meeting> getMeetingsByPlace(Room room) {
        List<Meeting> meetingsFilteredByPlace = new ArrayList<>();
        for(Meeting meeting : meetings) {
            if(meeting.getRoom() == room) {
                meetingsFilteredByPlace.add(meeting);
            }
        }
        // return meetingsFilteredByPlace;  // Unsorted
        return sortMeetingsByDate(sortMeetingsByStart(sortMeetingsByEnd(meetingsFilteredByPlace)));   // Sorted
    }

    @Override
    public List<Meeting> getMeetingsByDate(String date) {
        List<Meeting> meetingsFilteredByDate = new ArrayList<>();
        for(Meeting meeting : meetings) {
            if(meeting.getDate().equals(date)) {
                meetingsFilteredByDate.add(meeting);
            }
        }
        // return meetingsFilteredByDate;  // Unsorted
        return sortMeetingsByDate(sortMeetingsByStart(sortMeetingsByEnd(meetingsFilteredByDate)));   // Sorted
    }

    @Override
    public void deleteMeeting(Meeting meeting) {
        deleteRoomReservationSlot(meeting);
        meetings.remove(meeting);
    }

    @Override
    public void createMeeting(Meeting meeting) {
        createRoomReservationSlot(meeting);
        meetings.add(meeting);
    }

    @Override
    public List<Room> getRooms() {
        // return rooms;   // Unsorted
        return sortRoomsByName(rooms);  // Sorted
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
            if(room.getName().equals(name)) {
                return room;
            }
        }
        return null;
    }

    @Override
    public CharSequence[] getRoomsList() {
        CharSequence[] roomsList = new CharSequence[rooms.size()];
        for (int i = 0; i < rooms.size(); i++) {
            // roomsList[i] = rooms.get(i).getName();
            roomsList[i] = rooms.get(i).toString();    // Pour récupérer nom + capacité
        }
        return roomsList;
    }

    @Override
    public void deleteRoomReservationSlot(Meeting meeting) {
        try {
            Room slotRoom = meeting.getRoom();
            String slotDate = meeting.getDate();
            String slotStart = meeting.getStart();
            String slotEnd = meeting.getEnd();

            for (ReservationSlot slot : slotRoom.getReservationSlots()) {
                if (slot.getDate().equals(slotDate) && slot.getStart().equals(slotStart) && slot.getEnd().equals(slotEnd)) {
                    slotRoom.deleteReservationSlot(slot);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void createRoomReservationSlot(Meeting meeting) {
        try {
            Room slotRoom = meeting.getRoom();
            ReservationSlot slot = new ReservationSlot(System.currentTimeMillis(), meeting.getDate(), meeting.getStart(), meeting.getEnd());
            slotRoom.addReservationSlot(slot);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Meeting> sortMeetingsByDate(List<Meeting> meetings) {
        Collections.sort(meetings, Meeting.ComparatorDate);
        return meetings;
    }

    @Override
    public List<Meeting> sortMeetingsByStart(List<Meeting> meetings) {
        Collections.sort(meetings, Meeting.ComparatorStart);
        return meetings;
    }

    @Override
    public List<Meeting> sortMeetingsByEnd(List<Meeting> meetings) {
        Collections.sort(meetings, Meeting.ComparatorEnd);
        return meetings;
    }

    @Override
    public List<Room> sortRoomsByName(List<Room> rooms) {
        Collections.sort(rooms, Room.ComparatorName);
        return rooms;
    }
}
