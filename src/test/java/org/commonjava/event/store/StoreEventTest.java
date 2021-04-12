/**
 * Copyright (C) 2021 Red Hat, Inc. (https://github.com/Commonjava/indy-event-model)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
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
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonMap;
import static org.commonjava.event.store.EventStoreKey.fromString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class StoreEventTest
{
    ObjectMapper mapper;

    private final EventStoreKey key1 = new EventStoreKey( "maven", "group", "test1" );

    private final EventStoreKey key2 = new EventStoreKey( "maven", "group", "test2" );

    private final Set<EventStoreKey> keys = new HashSet<>();

    @BeforeEach
    public void prepare()
    {
        prepareObjMapper();
        keys.add( key1 );
        keys.add( key2 );
    }

    @AfterEach
    public void clear()
    {
        keys.clear();
    }

    private void prepareObjMapper()
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
    public void testMarshallForDeleteEvent()
            throws IOException
    {
        final IndyStoreEvent postDelete = new StorePostDeleteEvent( new EventMetadata(), keys );
        String json = mapper.writeValueAsString( postDelete );
        assertTrue( json.contains( "\"eventType\" : \"PostDelete\"" ) );
        assertTrue( json.contains( "eventMetadata" ) );
        assertTrue( json.contains( "maven:group:test1" ) );
        assertTrue( json.contains( "maven:group:test2" ) );

        final IndyStoreEvent preDelete = new StorePreDeleteEvent( new EventMetadata(), keys );
        json = mapper.writeValueAsString( preDelete );
        assertTrue( json.contains( "\"eventType\" : \"PreDelete\"" ) );
        assertTrue( json.contains( "eventMetadata" ) );
        assertTrue( json.contains( "maven:group:test1" ) );
        assertTrue( json.contains( "maven:group:test2" ) );

    }

    @Test
    public void testMarshallForEnable()
            throws IOException
    {
        final IndyStoreEvent enableEvent = new StoreEnablementEvent( new EventMetadata(), true, true, key1, key2 );
        final String json = mapper.writeValueAsString( enableEvent );
        assertTrue( json.contains( "\"eventType\" : \"Enablement\"" ) );
        assertTrue( json.contains( "eventMetadata" ) );
        assertTrue( json.contains( "maven:group:test1" ) );
        assertTrue( json.contains( "maven:group:test2" ) );
    }

    @Test
    public void testMarshallForUpdate()
            throws IOException
    {
        final IndyStoreEvent preCreate = new StorePreUpdateEvent( StoreUpdateType.ADD, new EventMetadata(),
                                                                  singletonMap( key1, Collections.emptyMap() ) );
        String json = mapper.writeValueAsString( preCreate );
        assertTrue( json.contains( "\"eventType\" : \"PreUpdate\"" ) );
        assertTrue( json.contains( "eventMetadata" ) );
        assertTrue( json.contains( "\"updateType\" : \"ADD\"" ) );
        assertTrue( json.contains( "maven:group:test1" ) );

        final IndyStoreEvent preUpdate = new StorePreUpdateEvent( StoreUpdateType.UPDATE, new EventMetadata(),
                                                                  singletonMap( key1, singletonMap( "description",
                                                                                                    asList( "new Desc",
                                                                                                            "old Desc" ) ) ) );
        json = mapper.writeValueAsString( preUpdate );
        assertTrue( json.contains( "\"eventType\" : \"PreUpdate\"" ) );
        assertTrue( json.contains( "eventMetadata" ) );
        assertTrue( json.contains( "\"updateType\" : \"UPDATE\"" ) );
        assertTrue( json.contains( "\"description\" : [ \"new Desc\", \"old Desc\" ]" ) );
        assertTrue( json.contains( "maven:group:test1" ) );

        final IndyStoreEvent postCreate = new StorePostUpdateEvent( StoreUpdateType.ADD, new EventMetadata(),
                                                                    singletonMap( key1, Collections.emptyMap() ) );
        json = mapper.writeValueAsString( postCreate );
        assertTrue( json.contains( "\"eventType\" : \"PostUpdate\"" ) );
        assertTrue( json.contains( "eventMetadata" ) );
        assertTrue( json.contains( "\"updateType\" : \"ADD\"" ) );
        assertTrue( json.contains( "maven:group:test1" ) );

        final IndyStoreEvent postUpdate = new StorePostUpdateEvent( StoreUpdateType.UPDATE, new EventMetadata(),
                                                                    singletonMap( key1, singletonMap( "description",
                                                                                                      asList( "new Desc",
                                                                                                              "old Desc" ) ) ) );
        json = mapper.writeValueAsString( postUpdate );
        assertTrue( json.contains( "\"eventType\" : \"PostUpdate\"" ) );
        assertTrue( json.contains( "eventMetadata" ) );
        assertTrue( json.contains( "\"updateType\" : \"UPDATE\"" ) );
        assertTrue( json.contains( "\"description\" : [ \"new Desc\", \"old Desc\" ]" ) );
        assertTrue( json.contains( "maven:group:test1" ) );
    }

    @Test
    public void testUnmarshallForDelete()
            throws Exception
    {
        String json =
                "{\"eventType\" : \"PostDelete\", \"keys\" : [ \"maven:group:test1\", \"maven:group:test2\" ]" + "}";
        StorePostDeleteEvent postDelete = mapper.readValue( json, StorePostDeleteEvent.class );
        assertSame( StoreEventType.PostDelete, postDelete.getEventType() );
        assertTrue( postDelete.getKeys().contains( fromString( "maven:group:test1" ) ) );
        assertTrue( postDelete.getKeys().contains( fromString( "maven:group:test2" ) ) );

        json = "{\"eventType\" : \"PreDelete\",  \"keys\" : [ \"maven:group:test1\", \"maven:group:test2\" ]" + "}";
        StorePreDeleteEvent preDelete = mapper.readValue( json, StorePreDeleteEvent.class );
        assertSame( StoreEventType.PreDelete, preDelete.getEventType() );
        assertTrue( preDelete.getKeys().contains( fromString( "maven:group:test1" ) ) );
        assertTrue( preDelete.getKeys().contains( fromString( "maven:group:test2" ) ) );
    }

    @Test
    public void testUnmarshallForEnablement()
            throws IOException
    {
        String json =
                "{\"disabling\" : true,\"preprocessing\" : true,\"keys\" : [ \"maven:group:test1\", \"maven:group:test2\" ],\"eventType\" : \"Enablement\"}";
        StoreEnablementEvent enablement = mapper.readValue( json, StoreEnablementEvent.class );
        assertSame( StoreEventType.Enablement, enablement.getEventType() );
        assertTrue( enablement.getKeys().contains( fromString( "maven:group:test1" ) ) );
        assertTrue( enablement.getKeys().contains( fromString( "maven:group:test2" ) ) );
        assertTrue( enablement.isDisabling() );
        assertTrue( enablement.isPreprocessing() );
    }

    @Test
    public void testUnmarshallForUpdate()
            throws IOException
    {
        String json =
                "{\"eventType\" : \"PreUpdate\", \"updateType\": \"ADD\",  \"keys\" : [ \"maven:group:test1\" ], \"eventMetadata\" : {"
                        + "\"pacakgeType\" : \"maven\"" + "  }," + "  \"changeMap\" : {" + "\"maven:group:test1\" : {}"
                        + "}}";
        StorePreUpdateEvent preCreate = mapper.readValue( json, StorePreUpdateEvent.class );
        assertSame( StoreEventType.PreUpdate, preCreate.getEventType() );
        assertSame( StoreUpdateType.ADD, preCreate.getType() );
        assertTrue( preCreate.getKeys().contains( fromString( "maven:group:test1" ) ) );
        assertTrue( preCreate.getChangeMap().containsKey( key1 ) );

        json =
                "{\"eventType\" : \"PreUpdate\", \"updateType\": \"UPDATE\",  \"keys\" : [ \"maven:group:test1\" ], \"eventMetadata\" : {"
                        + "\"pacakgeType\" : \"maven\"" + "  }," + "  \"changeMap\" : {" + "\"maven:group:test1\" : {"
                        + "\"description\" : [ \"new Desc\", \"old Desc\" ]" + "}" + "  }}";
        StorePreUpdateEvent preUpdate = mapper.readValue( json, StorePreUpdateEvent.class );
        assertSame( StoreEventType.PreUpdate, preUpdate.getEventType() );
        assertSame( StoreUpdateType.UPDATE, preUpdate.getType() );
        assertTrue( preUpdate.getKeys().contains( fromString( "maven:group:test1" ) ) );
        assertTrue( preUpdate.getChangeMap().containsKey( key1 ) );
        Map<String, List<Object>> changes = preUpdate.getChangeMap().get( key1 );
        assertTrue( changes.containsKey( "description" ) );
        assertEquals( "new Desc", changes.get( "description" ).get( 0 ) );
        assertEquals( "old Desc", changes.get( "description" ).get( 1 ) );

        json =
                "{\"eventType\" : \"PostUpdate\", \"updateType\": \"ADD\",  \"keys\" : [ \"maven:group:test1\" ], \"eventMetadata\" : {"
                        + "\"pacakgeType\" : \"maven\"" + "  }," + "  \"changeMap\" : {" + "\"maven:group:test1\" : {}"
                        + "}}";
        StorePostUpdateEvent postCreate = mapper.readValue( json, StorePostUpdateEvent.class );
        assertSame( StoreEventType.PostUpdate, postCreate.getEventType() );
        assertSame( StoreUpdateType.ADD, postCreate.getType() );
        assertTrue( postCreate.getKeys().contains( fromString( "maven:group:test1" ) ) );
        assertTrue( postCreate.getChangeMap().containsKey( key1 ) );

        json =
                "{\"eventType\" : \"PostUpdate\", \"updateType\": \"UPDATE\",  \"keys\" : [ \"maven:group:test1\" ], \"eventMetadata\" : {"
                        + "\"pacakgeType\" : \"maven\"" + "  }," + "  \"changeMap\" : {" + "\"maven:group:test1\" : {"
                        + "\"description\" : [ \"new Desc\", \"old Desc\" ]" + "}" + "  }}";
        StorePostUpdateEvent postUpdate = mapper.readValue( json, StorePostUpdateEvent.class );
        assertSame( StoreEventType.PostUpdate, postUpdate.getEventType() );
        assertSame( StoreUpdateType.UPDATE, postUpdate.getType() );
        assertTrue( postUpdate.getKeys().contains( fromString( "maven:group:test1" ) ) );
        assertTrue( postUpdate.getChangeMap().containsKey( key1 ) );
        changes = postUpdate.getChangeMap().get( key1 );
        assertTrue( changes.containsKey( "description" ) );
        assertEquals( "new Desc", changes.get( "description" ).get( 0 ) );
        assertEquals( "old Desc", changes.get( "description" ).get( 1 ) );

    }

}
