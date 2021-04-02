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
package org.commonjava.event.common;

import org.commonjava.event.util.SerializationUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class IndyEventTest
{

    @Test
    public void testSerializing_thenUseExternalizable() throws IOException, ClassNotFoundException
    {
        IndyEvent event = new IndyEvent();

        EventMetadata metadata = new EventMetadata();
        metadata.set( "k1", "v1" );
        event.setEventMetadata( metadata );

        IndyEvent event2 = SerializationUtil.doRoundTrip( event );

        Assertions.assertEquals( "v1", event2.getEventMetadata().get( "k1" ) );
    }

}
