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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.commonjava.event.common.EventMetadata;
import org.commonjava.event.store.jackson.EventStoreKeyModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class StoreEventTest
{
    ObjectMapper mapper;

    @BeforeEach
    public void prepare()
    {
        mapper = new ObjectMapper();
        mapper.setSerializationInclusion( JsonInclude.Include.NON_EMPTY );
        mapper.configure( JsonGenerator.Feature.AUTO_CLOSE_JSON_CONTENT, true );
        mapper.configure( DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true );

        mapper.enable( SerializationFeature.INDENT_OUTPUT, SerializationFeature.USE_EQUALITY_FOR_OBJECT_ID );
        mapper.enable( MapperFeature.AUTO_DETECT_FIELDS );

        mapper.disable( SerializationFeature.WRITE_NULL_MAP_VALUES, SerializationFeature.WRITE_EMPTY_JSON_ARRAYS );
        mapper.disable( DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES );
        mapper.disable( DeserializationFeature.FAIL_ON_NULL_CREATOR_PROPERTIES );
        mapper.registerModule( new EventStoreKeyModule() );
        mapper.registerSubtypes( EventStoreKey.class );
    }

    @Test
    public void testMarshall()
            throws IOException
    {
        final EventStoreKey key1 = new EventStoreKey( "maven", "group", "test1" );
        final EventStoreKey key2 = new EventStoreKey( "maven", "group", "test2" );
        final Set<EventStoreKey> keys = new HashSet<>();
        keys.add( key1 );
        keys.add( key2 );
        final IndyStoreEvent event = new StorePostDeleteEvent( new EventMetadata(), keys );
        String json = mapper.writeValueAsString( event );
        assertTrue( json.contains( "\"eventType\" : \"PostDelete\"" ) );
        assertTrue( json.contains( "maven:group:test1" ) );
        assertTrue( json.contains( "maven:group:test2" ) );

        final IndyStoreEvent enableEvent = new StoreEnablementEvent( new EventMetadata(), true, true, key1, key2 );
        json = mapper.writeValueAsString( enableEvent );
        assertTrue( json.contains( "\"eventType\" : \"Enablement\"" ) );
        assertTrue( json.contains( "maven:group:test1" ) );
        assertTrue( json.contains( "maven:group:test2" ) );
    }

    @Test
    public void testUnmarshall()
            throws Exception
    {
        String json =
                "{\"eventType\" : \"PostDelete\", \n \"keys\" : [ \"maven:group:test1\", \"maven:group:test2\" ]\n"
                        + "}";
        StorePostDeleteEvent postDelete = mapper.readValue( json, StorePostDeleteEvent.class );
        assertSame( StoreEventType.PostDelete, postDelete.getEventType() );
        assertTrue( postDelete.getKeys().contains( EventStoreKey.fromString( "maven:group:test1" ) ) );
        assertTrue( postDelete.getKeys().contains( EventStoreKey.fromString( "maven:group:test2" ) ) );

        json =
                "{\"disabling\" : true,\"preprocessing\" : true,\"keys\" : [ \"maven:group:test1\", \"maven:group:test2\" ],\"eventType\" : \"Enablement\"}";
        StoreEnablementEvent enablement = mapper.readValue( json, StoreEnablementEvent.class );
        assertSame( StoreEventType.Enablement, enablement.getEventType() );
        assertTrue( enablement.getKeys().contains( EventStoreKey.fromString( "maven:group:test1" ) ) );
        assertTrue( enablement.getKeys().contains( EventStoreKey.fromString( "maven:group:test2" ) ) );
        assertTrue( enablement.isDisabling() );
        assertTrue( enablement.isPreprocessing() );
    }

    @Test
    public void test() throws Exception{
        EventStoreKey key = EventStoreKey.fromString( "maven:group:test1" );
        System.out.println( mapper.writeValueAsString(new StoreEnablementEvent( null,true, false, key )));
    }

}
