package com.example.mareu;

import static com.example.mareu.utils.UnitTestsToolbox.clearLists;
import static com.example.mareu.utils.UnitTestsToolbox.fillMeetingsListWithDummyMeetings;
import static org.junit.Assert.*;

import com.example.mareu.di.DI;
import com.example.mareu.model.Meeting;
import com.example.mareu.model.Room;
import com.example.mareu.service.MeetingApiService;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// @RunWith(AndroidJUnit4.class)
public class OnMeetingActionsUnitTests {

    private final MeetingApiService service = DI.getMeetingApiService();
    private List<Meeting> myMeetingList;
    private List<Room> myRoomsList;
    private final Meeting mMeetingToAdd = new Meeting(101, "Nouvelle réunion",
            "17/02/2022", "17:00", "18:00", service.getRoomById(1),
            Arrays.asList("francis@lamzone.com", "patrick@lamzone.com"));

    @Before
    public void setup() {
        myMeetingList = service.getMeetings();
        myRoomsList = service.getRooms();
        fillMeetingsListWithDummyMeetings(myMeetingList, myRoomsList);
    }

    @After
    public void cleanup() {
        clearLists(myMeetingList, myRoomsList);
    }


    @Test
    public void createMeetingWithSuccess() {
        int listSize = myMeetingList.size();

        service.createMeeting(mMeetingToAdd);

        assertEquals(listSize + 1, myMeetingList.size());
    }

    @Test
    public void createSlotWithSuccessWhenMeetingIsCreated() {
        int slotListSize = mMeetingToAdd.getRoom().getReservationSlots().size();

        service.createMeeting(mMeetingToAdd);

        assertEquals(slotListSize + 1, mMeetingToAdd.getRoom().getReservationSlots().size());
    }

    @Test
    public void deleteMeetingWithSuccess() {
        int listSize = myMeetingList.size();
        Meeting meetingToDelete = myMeetingList.get(0);

        service.deleteMeeting(meetingToDelete);

        assertEquals(listSize - 1, myMeetingList.size());
    }

    @Test
    public void deleteSlotWithSuccessWhenMeetingIsDeleted() {
        Meeting mMeetingToDelete = myMeetingList.get(1);
        int slotListSize = mMeetingToDelete.getRoom().getReservationSlots().size();

        service.deleteMeeting(mMeetingToDelete);

        assertEquals(slotListSize - 1, mMeetingToDelete.getRoom().getReservationSlots().size());
    }

}
