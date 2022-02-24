package com.example.mareu.utils;

import com.example.mareu.di.DI;
import com.example.mareu.model.Meeting;
import com.example.mareu.model.ReservationSlot;
import com.example.mareu.model.Room;
import com.example.mareu.service.MeetingApiService;

import java.util.List;

public class UnitTestsToolbox {
    private static final MeetingApiService service = DI.getMeetingApiService();
    private static final List<Meeting> myDummyMeetingsList = service.getDummyMeetings();
    private static final List<Room> myDummyRoomsList = service.getDummyRooms();


    public static void fillMeetingsListWithDummyMeetings(List<Meeting> myMeetingList, List<Room> myRoomsList) {
        myMeetingList.addAll(myDummyMeetingsList);

        for (Room myDummyRoom : myDummyRoomsList) {
            int id = myDummyRoom.getId();
            for (Room myRoom : myRoomsList) {
                if (myRoom.getId() == id) {
                    List<ReservationSlot> myDummySlotsList = myDummyRoom.getReservationSlots();
                    for (ReservationSlot myDummySlot : myDummySlotsList) {
                        myRoom.addReservationSlot(myDummySlot);
                    }
                }
            }
        }
    }

    public static void clearLists(List<Meeting> myMeetingList, List<Room> myRoomsList) {
        for (Room myDummyRoom : myDummyRoomsList) {
            int id = myDummyRoom.getId();
            for (Room myRoom : myRoomsList) {
                if (myRoom.getId() == id) {
                    List<ReservationSlot> myDummySlotsList = myDummyRoom.getReservationSlots();
                    for (ReservationSlot myDummySlot : myDummySlotsList) {
                        myRoom.removeReservationSlot(myDummySlot);
                    }
                }
            }
        }

        myMeetingList.clear();
    }

}
