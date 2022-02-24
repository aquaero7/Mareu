package com.example.mareu.service;

import static com.example.mareu.service.DummyMeetingGenerator.generateDummyMeetings;
import static com.example.mareu.service.DummyMeetingGenerator.generateDummyRooms;
import static com.example.mareu.service.DummyMeetingGenerator.generateRooms;

import com.example.mareu.model.Meeting;
import com.example.mareu.model.ReservationSlot;
import com.example.mareu.model.Room;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DummyMeetingApiService implements MeetingApiService {

    /*  // Start application with dummy meetings already loaded
    private List<Meeting> meetings = getDummyMeetings();
    private List<Room> rooms = getDummyRooms();
    */

    //  // Start application with an empty meetings list
    private List<Meeting> meetings = new ArrayList<>();
    private List<Room> rooms = getLamzoneRooms();
    //


    // Get methods

    @Override
    public List<Meeting> getDummyMeetings() {
        // return generateDummyMeetings(); // Unsorted
        return sortMeetingsByDate(sortMeetingsByStart(sortMeetingsByEnd(generateDummyMeetings())));   // Sorted
    }

    @Override
    public List<Room> getDummyRooms() {
        // return generateDummyRooms();   // Unsorted
        return sortRoomsByName(generateDummyRooms());  // Sorted
    }

    @Override
    public List<Room> getLamzoneRooms() {
        // return generateRooms();   // Unsorted
        return sortRoomsByName(generateRooms());  // Sorted
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
    public CharSequence[] getRoomsItems() {
        CharSequence[] roomsList = new CharSequence[rooms.size()];
        for (int i = 0; i < rooms.size(); i++) {
            // roomsList[i] = rooms.get(i).getName();   // Pour récupérer seulement le nom
            roomsList[i] = rooms.get(i).toString();    // Pour récupérer nom + capacité
        }
        return roomsList;
    }


    // Creation ans deletion methods

    @Override
    public void deleteMeeting(Meeting meeting) {
        deleteRoomReservationSlot(meeting);

        try {
            meetings.remove(meeting);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Exception dans " + getClass() + " / " + this + " : " + e.getMessage());
        }
    }

    @Override
    public void createMeeting(Meeting meeting) {
        createRoomReservationSlot(meeting);
        try {
            meetings.add(meeting);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Exception dans " + getClass() + " / " + this + " : " + e.getMessage());
        }
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
                    slotRoom.removeReservationSlot(slot);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Exception dans " + getClass() + " / " + this + " : " + e.getMessage());
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
            System.out.println("Exception dans " + getClass() + " / " + this + " : " + e.getMessage());
        }
    }


    // Sort methods

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
