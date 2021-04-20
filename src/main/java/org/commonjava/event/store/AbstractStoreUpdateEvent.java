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
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.commonjava.event.common.EventMetadata;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Event signaling that one or more specified ArtifactStore instances' configurations were changed. The {@link StoreUpdateType}
 * gives more information about the nature of the update.
 */
public abstract class AbstractStoreUpdateEvent
        extends AbstractIndyStoreEvent
{

    @JsonProperty("updateType")
    private final StoreUpdateType updateType;

    @JsonProperty("changeMap")
    @JsonInclude
    private final Map<EventStoreKey, Map<String, List<Object>>> changeMap;

    protected AbstractStoreUpdateEvent( final StoreUpdateType type, final EventMetadata metadata,
                                        final Map<EventStoreKey, Map<String, List<Object>>> changeMap )
    {
        super( metadata, changeMap.keySet() );
        this.changeMap = changeMap;
        this.updateType = type;
    }

    public Map<EventStoreKey, Map<String, Object>> getOriginal( EventStoreKey storeKey )
    {
        final Map<String, List<Object>> changes = changeMap.get( storeKey );
        final Map<String, Object> original = new HashMap<>( changes.size() );
        for ( Map.Entry<String, List<Object>> change : changes.entrySet() )
        {
            original.put( change.getKey(), change.getValue().get( 1 ) );
        }
        return Collections.singletonMap( storeKey, original );
    }

    public Map<EventStoreKey, Map<String, List<Object>>> getChangeMap()
    {
        return changeMap;
    }

    /**
     * Return the type of update that took place.
     */
    public StoreUpdateType getUpdateType()
    {
        return updateType;
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
        return getClass().getSimpleName() + "{changed=" + getChanges() + ",type=" + updateType + '}';
    }
}
