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
package org.commonjava.event.promote;

import org.commonjava.event.common.EventMetadata;
import org.commonjava.event.util.SerializationUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class PathsPromoteCompleteEventTest
{

    @Test
    public void testSerializing_thenUseExternalizable() throws IOException, ClassNotFoundException
    {
        PathsPromoteCompleteEvent event = new PathsPromoteCompleteEvent();
        event.setPromotionId( "build-0001" );
        event.setSourceStore( "http://repo1.maven.org/maven2" );
        event.setTargetStore( "http://repo1.maven.org/maven2" );
        event.setSkippedPaths( new HashSet<>( List.of( "path/to/foo-1.pom" ) ) );
        event.setCompletedPaths( new HashSet<>( List.of( "path/to/foo-1.pom" ) ) );
        event.setPurgeSource( true );

        EventMetadata metadata = new EventMetadata();
        metadata.set( "k1", "v1" );
        event.setEventMetadata( metadata );

        PathsPromoteCompleteEvent event2 = SerializationUtil.doRoundTrip( event );

        Assertions.assertEquals( event.getPromotionId(), event2.getPromotionId() );
        Assertions.assertEquals( event.getSourceStore(), event2.getSourceStore() );
        Assertions.assertEquals( event.getTargetStore(), event2.getTargetStore() );
        Assertions.assertEquals( event.getSkippedPaths(), event2.getSkippedPaths() );
        Assertions.assertEquals( event.getCompletedPaths(), event2.getCompletedPaths() );
        Assertions.assertEquals( event.isPurgeSource(), event2.isPurgeSource() );
        Assertions.assertEquals( "v1", event2.getEventMetadata().get( "k1" ) );
    }

}
