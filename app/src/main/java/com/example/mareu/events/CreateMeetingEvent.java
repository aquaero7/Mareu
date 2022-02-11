package com.example.mareu.events;

import com.example.mareu.model.Meeting;

public class CreateMeetingEvent {
    /**
     * Meeting to create
     */
    public Meeting meeting;

    /**
     * Constructor.
     * @param meeting
     */
    public CreateMeetingEvent(Meeting meeting) {
        this.meeting = meeting;
    }
}
