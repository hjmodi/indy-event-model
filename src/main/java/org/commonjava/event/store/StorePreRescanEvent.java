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

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import org.commonjava.event.common.EventMetadata;

import java.util.Collection;

import static org.commonjava.event.store.StoreEventType.PreRescan;

public class StorePreRescanEvent
        extends StoreRescanEvent
{
    public StorePreRescanEvent( final EventMetadata eventMetadata, final EventStoreKey storeKey )
    {
        super( eventMetadata, storeKey );
    }

    public StorePreRescanEvent( final @JsonProperty( "eventMetadata" ) EventMetadata eventMetadata,
                                final @JsonProperty( "keys" ) Collection<EventStoreKey> storeKeys )
    {
        super( eventMetadata, storeKeys );
    }

    @Override
    public StoreEventType getEventType()
    {
        return PreRescan;
    }

    @JsonSetter( "eventType" )
    @Override
    public void setEventType( StoreEventType eventType )
    {
        checkEventType( PreRescan, eventType );
    }
}
