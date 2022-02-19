package com.example.mareu;

import static org.junit.Assert.*;

import com.example.mareu.di.DI;
import com.example.mareu.model.Meeting;
import com.example.mareu.service.MeetingApiService;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

public class OnMeetingActionsUnitTests {

    private final MeetingApiService service = DI.getMeetingApiService();
    private final Meeting mMeetingToAdd = new Meeting(101, "Nouvelle réunion",
            "17/02/2022", "17:00", "18:00", service.getRoomById(1),
            Arrays.asList("francis@lamzone.com", "patrick@lamzone.com"));

    @Before

    @Test
    public void createMeetingWithSuccess() {
        int listSize = service.getMeetings().size();

        service.createMeeting(mMeetingToAdd);

        assertEquals(listSize + 1, service.getMeetings().size());
    }

    @Test
    public void createSlotWithSuccessWhenMeetingIsCreated() {
        int slotListSize = mMeetingToAdd.getRoom().getReservationSlots().size();

        service.createMeeting(mMeetingToAdd);

        assertEquals(slotListSize + 1, mMeetingToAdd.getRoom().getReservationSlots().size());
    }

    @Test
    public void deleteMeetingWithSuccess() {
        int listSize = service.getMeetings().size();
        Meeting meetingToDelete = service.getMeetings().get(0);

        service.deleteMeeting(meetingToDelete);

        assertEquals(listSize - 1, service.getMeetings().size());
    }

    @Test
    public void deleteSlotWithSuccessWhenMeetingIsDeleted() {
        Meeting mMeetingToDelete = service.getMeetings().get(0);
        int slotListSize = mMeetingToDelete.getRoom().getReservationSlots().size();

        service.deleteMeeting(mMeetingToDelete);

        assertEquals(slotListSize - 1, mMeetingToDelete.getRoom().getReservationSlots().size());
    }

}
