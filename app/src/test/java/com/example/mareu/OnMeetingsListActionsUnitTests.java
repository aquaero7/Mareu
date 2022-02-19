package com.example.mareu;

import static org.junit.Assert.*;

import com.example.mareu.di.DI;
import com.example.mareu.model.Meeting;
import com.example.mareu.model.Room;
import com.example.mareu.service.MeetingApiService;

import org.hamcrest.MatcherAssert;
import org.hamcrest.collection.IsIterableContainingInAnyOrder;
import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OnMeetingsListActionsUnitTests {

    private final MeetingApiService service = DI.getMeetingApiService();
    private final List<Meeting> myDummyList = service.getDummyMeetings();
    private List<Meeting> myMeetingList = new ArrayList<>();
    private final SimpleDateFormat dfDate = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE);
    private final SimpleDateFormat dfDateLong = new SimpleDateFormat("EEEE dd MMMM yyyy", Locale.FRANCE);

    @Before
    /*
    public void setup() {
        // service = DI.getMeetingApiService();
    }
    */

    @Test
    public void getMeetingsListWithSuccess() {
        final int DUMMY_MEETINGS_SIZE = 20;
        myMeetingList = service.getMeetings();

        assertEquals (DUMMY_MEETINGS_SIZE, myDummyList.size());
        assertEquals (myDummyList.size(), myMeetingList.size());
        MatcherAssert.assertThat(myMeetingList, IsIterableContainingInAnyOrder.containsInAnyOrder(myDummyList.toArray()));
    }

    @Test
    public void listMeetingNameFieldIsNotEmpty() {
        myMeetingList = service.getMeetings();

        for (Meeting mMeeting : myMeetingList) {
            assertFalse(mMeeting.getName().isEmpty());
        }
    }

    @Test
    public void listMeetingDateFieldIsNotEmpty() {
        myMeetingList = service.getMeetings();

        for (Meeting mMeeting : myMeetingList) {
            assertFalse(mMeeting.getDate().isEmpty());
        }
    }

    @Test
    public void listMeetingStartFieldIsNotEmpty() {
        myMeetingList = service.getMeetings();

        for (Meeting mMeeting : myMeetingList) {
            assertFalse(mMeeting.getStart().isEmpty());
        }
    }

    @Test
    public void listMeetingEndFieldIsNotEmpty() {
        myMeetingList = service.getMeetings();

        for (Meeting mMeeting : myMeetingList) {
            assertFalse(mMeeting.getEnd().isEmpty());
        }
    }

    @Test
    public void listMeetingRoomFieldIsNotEmpty() {
        myMeetingList = service.getMeetings();

        for (Meeting mMeeting : myMeetingList) {
            assertNotNull(mMeeting.getRoom());
            assertFalse(mMeeting.getRoom().getName().isEmpty());
        }
    }

    @Test
    public void listMeetingParticipantFieldIsNotEmpty() {
        myMeetingList = service.getMeetings();

        for (Meeting mMeeting : myMeetingList) {
            assertNotNull(mMeeting.getParticipants());
            assertFalse(mMeeting.getParticipantsToString().isEmpty());
        }
    }

    @Test
    public void listMeetingIsCorrectlySorted() {
        myMeetingList = service.getMeetings();

        for (int i = 0; i < myMeetingList.size()-1; i++) {
            try {
                Date date1 = dfDateLong.parse(myMeetingList.get(i).getDate());
                Date date2 = dfDateLong.parse(myMeetingList.get(i+1).getDate());
                assert date1 != null;

                assertTrue(date1.compareTo(date2) >= 0);
                if (date1 == date2) {
                    assertTrue(myMeetingList.get(i).getStart().compareTo(myMeetingList.get(i + 1).getStart()) <= 0);
                    assertTrue(myMeetingList.get(i).getEnd().compareTo(myMeetingList.get(i + 1).getEnd()) <= 0);
                }
            } catch (ParseException e) {
                e.printStackTrace();
                System.out.println("ParseException in " + getClass() + " / " + this + " : " + e.getMessage());
            }
        }
    }

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
