package com.example.mareu;

import static com.example.mareu.utils.UnitTestsToolbox.clearLists;
import static com.example.mareu.utils.UnitTestsToolbox.fillMeetingsListWithDummyMeetings;
import static org.junit.Assert.*;

import com.example.mareu.di.DI;
import com.example.mareu.model.Meeting;
import com.example.mareu.model.Room;
import com.example.mareu.service.DummyMeetingGenerator;
import com.example.mareu.service.MeetingApiService;

import org.hamcrest.MatcherAssert;
import org.hamcrest.collection.IsIterableContainingInAnyOrder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

// @RunWith(AndroidJUnit4.class)
public class OnMeetingsListActionsUnitTests {

    private final MeetingApiService service = DI.getMeetingApiService();
    private final List<Meeting> myDummyList = service.getDummyMeetings();
    private List<Meeting> myMeetingList;
    private List<Room> myRoomsList;
    private final SimpleDateFormat dfDate = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE);
    private final SimpleDateFormat dfDateLong = new SimpleDateFormat("EEEE dd MMMM yyyy", Locale.FRANCE);

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
    public void meetingsListIsEmptyOnStart() {
        List<Meeting> myMeetingListOnStart = DI.getNewMeetingApiService().getMeetings();
        assertTrue(myMeetingListOnStart.isEmpty());
    }

    @Test
    public void getMeetingsListWithSuccess() {
        MatcherAssert.assertThat(myMeetingList, IsIterableContainingInAnyOrder.containsInAnyOrder(myDummyList.toArray()));
        assertEquals (myDummyList.size(), myMeetingList.size());
        for (int pos = 0; pos < myMeetingList.size(); pos++) {
            assertEquals (myDummyList.get(pos).getName(), myMeetingList.get(pos).getName());
            assertEquals (myDummyList.get(pos).getDate(), myMeetingList.get(pos).getDate());
            assertEquals (myDummyList.get(pos).getStart(), myMeetingList.get(pos).getStart());
            assertEquals (myDummyList.get(pos).getEnd(), myMeetingList.get(pos).getEnd());
            assertEquals (myDummyList.get(pos).getRoom(), myMeetingList.get(pos).getRoom());
            assertEquals (myDummyList.get(pos).getParticipants(), myMeetingList.get(pos).getParticipants());
        }
    }

    @Test
    public void fieldsDisplayedInListAreNotEmpty() {
        for (Meeting mMeeting : myMeetingList) {
            assertFalse(mMeeting.getName().isEmpty());
            assertFalse(mMeeting.getDate().isEmpty());
            assertFalse(mMeeting.getStart().isEmpty());
            assertFalse(mMeeting.getEnd().isEmpty());
            assertNotNull(mMeeting.getRoom());
            assertFalse(mMeeting.getRoom().getName().isEmpty());
            assertNotNull(mMeeting.getParticipants());
            assertFalse(mMeeting.getParticipantsToString().isEmpty());
        }
    }

    @Test
    public void listMeetingIsCorrectlySorted() {
        // Check that origin list is unsorted
        List<Meeting> myUnsortedList = DummyMeetingGenerator.DUMMY_MEETINGS;
        boolean unSortedFlag = false;

        for (int i = 0; i < myUnsortedList.size()-1; i++) {
            try {
                Date date1 = dfDate.parse(myUnsortedList.get(i).getDate());
                Date date2 = dfDate.parse(myUnsortedList.get(i + 1).getDate());

                assert date1 != null;
                if (date1.compareTo(date2) < 0) {
                    unSortedFlag = true;
                    break;
                } else if (date1 == date2) {
                    if (myUnsortedList.get(i).getStart().compareTo(myUnsortedList.get(i + 1).getStart()) > 0) {
                        unSortedFlag = true;
                        break;
                    } else if (myUnsortedList.get(i).getStart().equals(myUnsortedList.get(i + 1).getStart())) {
                        if (myUnsortedList.get(i).getEnd().compareTo(myUnsortedList.get(i + 1).getEnd()) > 0) {
                            unSortedFlag = true;
                            break;
                        }
                    }
                }
            } catch (ParseException e) {
                e.printStackTrace();
                System.out.println("ParseException in " + getClass() + " / " + this + " : " + e.getMessage());
            }
        assertTrue(unSortedFlag);
        }

        // Check that displayed list is sorted as expected
        List<Meeting> mySortedList = myMeetingList;

        for (int i = 0; i < mySortedList.size()-1; i++) {
            try {
                Date date1 = dfDate.parse(mySortedList.get(i).getDate());
                Date date2 = dfDate.parse(mySortedList.get(i+1).getDate());

                assert date1 != null;
                assertTrue(date1.compareTo(date2) >= 0);
                if (date1 == date2) {
                    assertTrue(mySortedList.get(i).getStart().compareTo(mySortedList.get(i + 1).getStart()) <= 0);
                    if (mySortedList.get(i).getStart().equals(mySortedList.get(i + 1).getStart())) {
                        assertTrue(mySortedList.get(i).getEnd().compareTo(mySortedList.get(i + 1).getEnd()) <= 0);
                    }
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
