package org.commonjava.event.file;

import org.commonjava.event.common.EventMetadata;
import org.commonjava.event.util.SerializationUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class FileEventTest
{

    @Test
    public void testSerializing_thenUseExternalizable() throws IOException, ClassNotFoundException
    {
        FileEvent event = new FileEvent("build-0001");

        EventMetadata metadata = new EventMetadata();
        metadata.set( "k1", "v1" );
        event.setEventMetadata( metadata );

        FileEvent event2 = SerializationUtil.doRoundTrip( event );

        Assertions.assertEquals( "build-0001", event2.getTrackingID() );
        Assertions.assertEquals( "v1", event2.getEventMetadata().get( "k1" ) );
    }

}
