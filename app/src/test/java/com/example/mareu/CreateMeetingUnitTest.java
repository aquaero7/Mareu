package com.example.mareu;

import static org.junit.Assert.*;

import com.example.mareu.di.DI;
import com.example.mareu.model.Meeting;
import com.example.mareu.service.MeetingApiService;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

public class CreateMeetingUnitTest {

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

}
