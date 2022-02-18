package com.example.mareu;

import static org.junit.Assert.*;

import com.example.mareu.di.DI;
import com.example.mareu.model.Meeting;
import com.example.mareu.service.MeetingApiService;

import org.junit.Before;
import org.junit.Test;

public class DeleteMeetingUnitTest {

    private final MeetingApiService service = DI.getMeetingApiService();

    @Before

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
