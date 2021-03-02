package org.commonjava.event.common;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.UUID;

public class IndyEvent implements Externalizable
{

    private static final int VERSION = 1;

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

    public void writeExternal( ObjectOutput out ) throws IOException
    {
        out.writeObject( Integer.toString( VERSION ) );
        out.writeObject( eventID );
        out.writeObject( eventMetadata );
    }

    public void readExternal( ObjectInput in ) throws IOException, ClassNotFoundException
    {
        int version = Integer.parseInt( ( String )in.readObject() );
        if ( version > VERSION )
        {
            throw new IOException(
                            "Cannot deserialize. This class is of an older version: " + VERSION +
                                            " vs. the version read from the data stream: " + version + "." );
        }

        this.eventID = (UUID) in.readObject();
        this.eventMetadata = (EventMetadata) in.readObject();

    }

    @Override
    public String toString()
    {
        return "IndyEvent{" + "eventID=" + eventID + ", eventMetadata=" + eventMetadata + '}';
    }
}
