/**
 * Copyright (C) 2021 Red Hat, Inc. (https://github.com/Commonjava/indy-event-model)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
