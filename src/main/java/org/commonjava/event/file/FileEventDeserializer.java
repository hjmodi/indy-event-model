package org.commonjava.event.file;

import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;

public class FileEventDeserializer extends ObjectMapperDeserializer<FileEvent>
{
    public FileEventDeserializer( )
    {
        super( FileEvent.class );
    }
}
