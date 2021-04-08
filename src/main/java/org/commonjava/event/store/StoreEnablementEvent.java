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
package org.commonjava.event.store;

import com.fasterxml.jackson.annotation.JsonSetter;
import org.commonjava.event.common.EventMetadata;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Spliterator;
import java.util.function.Consumer;

import static org.commonjava.event.store.StoreEventType.Enablement;

public class StoreEnablementEvent
        implements IndyStoreEvent
{

    private EventMetadata eventMetadata;

    private boolean disabling;

    private Set<EventStoreKey> storeKeys;

    private boolean preprocessing;

    StoreEnablementEvent()
    {
    }

    public StoreEnablementEvent( EventMetadata eventMetadata, boolean preprocessing, boolean disabling,
                                 EventStoreKey... storeKeys )
    {
        this.eventMetadata = eventMetadata;
        this.preprocessing = preprocessing;
        this.disabling = disabling;
        this.storeKeys = new HashSet<>( Arrays.asList( storeKeys ) );
    }

    public boolean isDisabling()
    {
        return disabling;
    }

    public boolean isPreprocessing()
    {
        return preprocessing;
    }

    @Override
    public EventMetadata getEventMetadata()
    {
        return eventMetadata;
    }

    @JsonSetter( "eventMetadata" )
    public void setEventMetadata( EventMetadata eventMetadata )
    {
        this.eventMetadata = eventMetadata;
    }

    @Override
    public StoreEventType getEventType()
    {
        return Enablement;
    }

    @JsonSetter( "eventType" )
    public final void setEventType( StoreEventType eventType )
    {
        if ( Enablement != eventType )
        {
            throw new IllegalArgumentException(
                    String.format( "Wrong event type! Should be %s but is %s", Enablement, eventType ) );
        }
    }

    @Override
    public Set<EventStoreKey> getKeys()
    {
        return storeKeys;
    }

    @JsonSetter( "keys" )
    public void setKeys( Collection<EventStoreKey> keys )
    {
        this.storeKeys = new HashSet<>( keys );
    }

    @Override
    public Iterator<EventStoreKey> iterator()
    {
        return storeKeys.iterator();
    }

    @Override
    public void forEach( Consumer<? super EventStoreKey> action )
    {
        storeKeys.forEach( action );
    }

    @Override
    public Spliterator<EventStoreKey> spliterator()
    {
        return storeKeys.spliterator();
    }

    @Override
    public String toString()
    {
        return "ArtifactStoreEnablementEvent{ disabling=" + disabling + ", stores=" + storeKeys + ", preprocessing="
                + preprocessing + '}';
    }
}
