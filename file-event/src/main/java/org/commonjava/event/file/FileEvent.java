package org.commonjava.event.file;

import org.commonjava.event.IndyEvent;

public class FileEvent extends IndyEvent
{

    private String trackingID;

    public FileEvent()
    {
        super();
    }

    public FileEvent( String trackingID )
    {
        super();
        this.trackingID = trackingID;
    }

    public String getTrackingID()
    {
        return trackingID;
    }

    public void setTrackingID( String trackingID )
    {
        this.trackingID = trackingID;
    }
}
