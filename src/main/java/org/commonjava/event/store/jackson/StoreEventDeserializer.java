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
package org.commonjava.event.store.jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.arc.Arc;
import io.quarkus.arc.ArcContainer;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.common.serialization.Deserializer;
import org.commonjava.event.store.IndyStoreEvent;
import org.commonjava.event.store.StoreEnablementEvent;
import org.commonjava.event.store.StoreEventType;
import org.commonjava.event.store.StorePostDeleteEvent;
import org.commonjava.event.store.StorePostRescanEvent;
import org.commonjava.event.store.StorePostUpdateEvent;
import org.commonjava.event.store.StorePreDeleteEvent;
import org.commonjava.event.store.StorePreRescanEvent;
import org.commonjava.event.store.StorePreUpdateEvent;
import org.commonjava.event.store.StoreRescanEvent;

import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StoreEventDeserializer
        implements Deserializer<IndyStoreEvent>
{
    private final ObjectMapper objectMapper = getObjectMapper();

    @Override
    public IndyStoreEvent deserialize( String topic, byte[] data )
    {
        if ( data == null )
        {
            return null;
        }
        final String payload = new String( data, Charset.defaultCharset() );

        final StoreEventType eventType = checkAndGetEventType( payload );
        Class<? extends IndyStoreEvent> type = decideEventClassType( eventType );
        try
        {
            return objectMapper.readValue( payload, type );
        }
        catch ( JsonProcessingException e )
        {
            throw new RuntimeException( e );
        }

    }

    private final Pattern pattern = Pattern.compile( "\"eventType\"\\s.*:\\s.*\"(\\w.+)\"" );

    private StoreEventType checkAndGetEventType( final String payload )
    {
        if ( StringUtils.isBlank( payload ) )
        {
            throw new IllegalArgumentException(
                    "The event is not a store event because it is empty, please check if the event message format is correct" );
        }
        final Matcher matcher = pattern.matcher( payload );
        RuntimeException wrongStoreEventException = new IllegalArgumentException(
                "The event is not a store event because it misses event type, please check if the event message format is correct" );
        if ( !matcher.find() )
        {
            throw wrongStoreEventException;
        }
        final String eventType;
        try
        {
            eventType = matcher.group( 1 );
        }
        catch ( RuntimeException e )
        {
            throw wrongStoreEventException;
        }

        if ( eventType == null )
        {
            throw wrongStoreEventException;
        }

        try
        {
            return StoreEventType.valueOf( eventType );
        }
        catch ( IllegalArgumentException e )
        {
            throw new IllegalArgumentException( String.format( "The event type %s is not a valid type.", eventType ) );
        }
    }

    private Class<? extends IndyStoreEvent> decideEventClassType( final StoreEventType eventType )
    {
        switch ( eventType )
        {
            case PreUpdate:
                return StorePreUpdateEvent.class;
            case PostUpdate:
                return StorePostUpdateEvent.class;
            case PreDelete:
                return StorePreDeleteEvent.class;
            case PostDelete:
                return StorePostDeleteEvent.class;
            case Rescan:
                return StoreRescanEvent.class;
            case PreRescan:
                return StorePreRescanEvent.class;
            case PostRescan:
                return StorePostRescanEvent.class;
            case Enablement:
                return StoreEnablementEvent.class;
            default:
                throw new IllegalArgumentException(
                        String.format( "The event type %s is not a valid type.", eventType ) );
        }
    }

    @Override
    public void close()
    {
    }

    private static ObjectMapper getObjectMapper()
    {
        ObjectMapper objectMapper = null;
        ArcContainer container = Arc.container();
        if ( container != null )
        {
            objectMapper = container.instance( ObjectMapper.class ).get();
        }
        return objectMapper != null ? objectMapper : new ObjectMapper();
    }
}

