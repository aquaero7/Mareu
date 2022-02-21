package com.example.mareu.di;

import com.example.mareu.service.DummyMeetingApiService;
import com.example.mareu.service.MeetingApiService;

public class DI {

    private static final MeetingApiService service = new DummyMeetingApiService();

    /**
     * Get an instance on @{@link MeetingApiService}
     * @return
     */
    public static MeetingApiService getMeetingApiService() {
        return service;
    }


    /**
     * Get always a new instance on @{@link MeetingApiService}. Useful for tests, so we ensure the context is clean.
     * @return
     */
    public static MeetingApiService getNewMeetingApiService() {
        return new DummyMeetingApiService();
    }

}
