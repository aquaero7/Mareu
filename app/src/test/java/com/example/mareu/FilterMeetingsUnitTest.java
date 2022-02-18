package com.example.mareu;

import static org.junit.Assert.*;

import com.example.mareu.di.DI;
import com.example.mareu.model.Meeting;
import com.example.mareu.model.Room;
import com.example.mareu.service.MeetingApiService;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class FilterMeetingsUnitTest {

    private final MeetingApiService service = DI.getMeetingApiService();

    @Before

    @Test
    public void filterMeetingsByDateWithSuccess() {
        String dateFilter = "23/02/2022";
        List<Meeting> filteredMeetings = service.getMeetingsByDate(dateFilter);

        for (Meeting mMeeting : filteredMeetings) {
            assertEquals(dateFilter,mMeeting.getDate());
        }
    }

    @Test
    public void filterMeetingsByPlaceWithSuccess() {
        Room roomFilter = service.getDummyRooms().get(0);
        List<Meeting> filteredMeetings = service.getMeetingsByPlace(roomFilter);

        for (Meeting mMeeting : filteredMeetings) {
            assertEquals(roomFilter,mMeeting.getRoom());
        }
    }

}
