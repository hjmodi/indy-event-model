package org.commonjava.event.file;

import org.commonjava.event.common.IndyEvent;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class FileEvent extends IndyEvent implements Externalizable
{

    private static final int FILE_EVENT_VERSION = 1;

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

    public void writeExternal( ObjectOutput out ) throws IOException
    {
        super.writeExternal( out );

        out.writeObject( Integer.toString( FILE_EVENT_VERSION ) );
        out.writeObject( trackingID );

    }

    public void readExternal( ObjectInput in ) throws IOException, ClassNotFoundException
    {
        super.readExternal( in );

        int version = Integer.parseInt( ( String )in.readObject() );
        if ( version > FILE_EVENT_VERSION )
        {
            throw new IOException(
                            "Cannot deserialize. This class is of an older version: " + FILE_EVENT_VERSION +
                                            " vs. the version read from the data stream: " + version + "." );
        }

        this.trackingID = (String) in.readObject();

    }

}
