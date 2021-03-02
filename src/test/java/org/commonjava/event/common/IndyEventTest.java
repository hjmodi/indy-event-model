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
