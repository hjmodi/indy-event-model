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

import com.fasterxml.jackson.annotation.JsonSetter;
import org.commonjava.event.common.EventMetadata;
import org.commonjava.event.common.IndyEvent;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Base class for events related to changes in Indy ArtifactStore definitions.
 */
public abstract class AbstractIndyStoreEvent
        extends IndyEvent
        implements IndyStoreEvent
{

    protected Collection<EventStoreKey> storeKeys;

    protected AbstractIndyStoreEvent()
    {
    }

    protected AbstractIndyStoreEvent( final EventMetadata eventMetadata, final Collection<EventStoreKey> storeKeys )
    {
        this.storeKeys = storeKeys == null ? Collections.emptySet() : clearNulls( storeKeys );
        setEventMetadata( eventMetadata );
    }

    protected AbstractIndyStoreEvent( final EventMetadata eventMetadata, final EventStoreKey... keys )
    {
        this.storeKeys =
                keys == null || keys.length == 0 ? Collections.emptySet() : clearNulls( Arrays.asList( keys ) );
        setEventMetadata( eventMetadata );
    }

    public static Collection<EventStoreKey> clearNulls( final Collection<EventStoreKey> storeKeys )
    {
        return storeKeys.stream().filter( Objects::nonNull ).collect( Collectors.toSet() );
    }

    @Override
    public final Collection<EventStoreKey> getKeys()
    {
        return storeKeys;
    }

    @JsonSetter( "keys" )
    public final void setKeys( Collection<EventStoreKey> keys )
    {
        this.storeKeys = keys;
    }

    @Override
    public final Iterator<EventStoreKey> iterator()
    {
        return storeKeys.iterator();
    }

    protected void checkEventType( StoreEventType expected, StoreEventType actual )
    {
        if ( expected != actual )
        {
            throw new IllegalArgumentException(
                    String.format( "Wrong event type! Should be %s but is %s", expected, actual ) );
        }
    }

}
