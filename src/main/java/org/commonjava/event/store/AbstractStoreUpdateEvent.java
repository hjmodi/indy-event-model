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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.commonjava.event.common.EventMetadata;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Event signaling that one or more specified ArtifactStore instances' configurations were changed. The {@link StoreUpdateType}
 * gives more information about the nature of the update.
 */
public abstract class AbstractStoreUpdateEvent
        extends AbstractIndyStoreEvent
{

    @JsonProperty("updateType")
    private final StoreUpdateType type;

    @JsonProperty("changeMap")
    private final Map<EventStoreKey, EventStoreKey> changeMap;

    protected AbstractStoreUpdateEvent( final StoreUpdateType type, final EventMetadata metadata,
                                        final Map<EventStoreKey, EventStoreKey> changeMap )
    {
        super( metadata, changeMap.keySet() );
        this.changeMap = cloneOriginals( changeMap );
        this.type = type;
    }

    private Map<EventStoreKey, EventStoreKey> cloneOriginals( Map<EventStoreKey, EventStoreKey> changeMap )
    {
        Map<EventStoreKey, EventStoreKey> cleaned = new HashMap<>();
        changeMap.forEach( ( key, value ) -> {
            if ( key != null && value != null )
            {
                cleaned.put( key, value.copyOf() );
            }
        } );

        return cleaned;
    }

    public EventStoreKey getOriginal( EventStoreKey store )
    {
        return changeMap.get( store );
    }

    public Map<EventStoreKey, EventStoreKey> getChangeMap()
    {
        return changeMap;
    }

    /**
     * Return the type of update that took place.
     */
    public StoreUpdateType getType()
    {
        return type;
    }

    /**
     * Return the changed ArtifactStore's specified in this event.
     */
    @JsonIgnore
    public Collection<EventStoreKey> getChanges()
    {
        return getKeys();
    }

    @Override
    public String toString()
    {
        return getClass().getSimpleName() + "{changed=" + getChanges() + ",type=" + type + '}';
    }
}
