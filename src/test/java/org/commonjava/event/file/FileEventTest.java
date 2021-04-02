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
package org.commonjava.event.file;

import org.commonjava.event.common.EventMetadata;
import org.commonjava.event.util.SerializationUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Date;

public class FileEventTest
{

    @Test
    public void testSerializing_thenUseExternalizable() throws IOException, ClassNotFoundException
    {
        FileEvent event = new FileEvent();
        event.setSessionId( "build-0001" );
        event.setSourceLocation( "http://repo1.maven.org/maven2" );
        event.setSourcePath( "org/foo-1.pom" );
        event.setEventType( FileEventType.ACCESS );
        event.setTimestamp( new Date() );

        EventMetadata metadata = new EventMetadata();
        metadata.set( "k1", "v1" );
        event.setEventMetadata( metadata );

        FileEvent event2 = SerializationUtil.doRoundTrip( event );

        Assertions.assertEquals( event.getSessionId(), event2.getSessionId() );
        Assertions.assertEquals( FileEventType.ACCESS, event2.getEventType() );
        Assertions.assertEquals( event.getSourceLocation(), event2.getSourceLocation() );
        Assertions.assertEquals( event.getSourcePath(), event2.getSourcePath() );
        Assertions.assertEquals( "v1", event2.getEventMetadata().get( "k1" ) );
    }

}
