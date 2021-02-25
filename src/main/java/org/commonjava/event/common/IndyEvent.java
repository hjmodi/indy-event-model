package org.commonjava.event.common;

import org.commonjava.event.common.EventMetadata;

import java.util.UUID;

public class IndyEvent
{

    private UUID eventID;

    private EventMetadata eventMetadata;

    public IndyEvent()
    {
        eventID = UUID.randomUUID();
    }

    public EventMetadata getEventMetadata()
    {
        return eventMetadata;
    }

    public void setEventMetadata( EventMetadata eventMetadata )
    {
        this.eventMetadata = eventMetadata;
    }

}
