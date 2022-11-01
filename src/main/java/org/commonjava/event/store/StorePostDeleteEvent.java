/**
 * Copyright (C) 2022 Red Hat, Inc. (https://github.com/Commonjava/indy-event-model)
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
package org.commonjava.event.store;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import org.commonjava.event.common.EventMetadata;

import java.util.Set;

import static org.commonjava.event.store.StoreEventType.PostDelete;

/**
 * Event signaling the deletion of one or more ArtifactStore instances is COMPLETE. This event will always contain the same type of store, when there is
 * more than one. Instance names are collected and available via getNames(), while the store type is available separately via the getType() method.
 * <br/>
 * As opposed to the ArtifactStore, this one MAY run asynchronously to avoid performance penalties for the user..
 */
public class StorePostDeleteEvent
        extends AbstractIndyStoreEvent
{
    public StorePostDeleteEvent( final @JsonProperty( "eventMetadata" ) EventMetadata eventMetadata,
                                 final @JsonProperty( "keys" ) Set<EventStoreKey> stores )
    {
        super( eventMetadata, stores );
    }

    @Override
    public final StoreEventType getEventType()
    {
        return PostDelete;
    }

    @JsonSetter( "eventType" )
    public final void setEventType( StoreEventType eventType )
    {
        checkEventType( PostDelete, eventType );
    }

    @Override
    public String toString()
    {
        return String.format( "%s", getClass().getSimpleName() );
    }
}
