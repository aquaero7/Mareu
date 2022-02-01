package com.example.mareu.di;

import com.example.mareu.service.DummyMeetingApiService;
import com.example.mareu.service.MeetingApiService;

public class DI {

    private static MeetingApiService service = new DummyMeetingApiService();

    /**
     * Get an instance on @{@link MeetingApiService}
     * @return
     */

    public static MeetingApiService getMeetingApiService() {
        return service;
    }


}
