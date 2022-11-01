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
package org.commonjava.event.file;

import org.commonjava.event.common.IndyEvent;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Date;

public class FileEvent extends IndyEvent implements Externalizable
{

    private static final int FILE_EVENT_VERSION = 1;

    private String sessionId;

    private String nodeId;

    private String checksum;

    private String targetLocation;

    private String targetPath;

    private FileEventType eventType;

    private String requestId;

    private Integer eventVersion;

    private String sourceLocation;

    private String sourcePath;

    private String md5;

    private Long size;

    private String sha1;

    private String storeKey;

    private Date timestamp;

    public FileEvent()
    {
        super();
        this.eventVersion = FILE_EVENT_VERSION;
    }

    public FileEvent( FileEventType eventType )
    {
        this();
        this.eventType = eventType;
    }

    public String getSessionId() { return sessionId; }

    public void setSessionId( String sessionId ) { this.sessionId = sessionId; }

    public String getNodeId() { return nodeId; }

    public void setNodeId( String nodeId ) { this.nodeId = nodeId; }

    public String getChecksum() { return checksum; }

    public void setChecksum( String checksum ) { this.checksum = checksum; }

    public String getTargetLocation() { return targetLocation; }

    public void setTargetLocation( String targetLocation ) { this.targetLocation = targetLocation; }

    public String getTargetPath() { return targetPath; }

    public void setTargetPath( String targetPath ) { this.targetPath = targetPath; }

    public FileEventType getEventType() { return eventType; }

    public void setEventType( FileEventType eventType ) { this.eventType = eventType; }

    public String getRequestId() { return requestId; }

    public void setRequestId( String requestId ) { this.requestId = requestId; }

    public Integer getEventVersion() { return eventVersion; }

    public void setEventVersion( Integer eventVersion ) { this.eventVersion = eventVersion; }

    public Date getTimestamp() { return timestamp; }

    public void setTimestamp( Date timestamp ) { this.timestamp = timestamp; }

    public String getSourceLocation() { return sourceLocation; }

    public void setSourceLocation( String sourceLocation ) { this.sourceLocation = sourceLocation; }

    public String getSourcePath() { return sourcePath; }

    public void setSourcePath( String sourcePath ) { this.sourcePath = sourcePath; }

    public String getMd5() { return md5; }

    public void setMd5( String md5 ) { this.md5 = md5; }

    public Long getSize() { return size; }

    public void setSize( Long size ) { this.size = size; }

    public String getSha1() { return sha1; }

    public void setSha1( String sha1 ) { this.sha1 = sha1; }

    public String getStoreKey() { return storeKey; }

    public void setStoreKey( String storeKey ) { this.storeKey = storeKey; }

    public int compareTo( FileEvent o )
    {
        return getTimestamp().compareTo( o.getTimestamp() );
    }

    @Override
    public void writeExternal( ObjectOutput out ) throws IOException
    {
        super.writeExternal( out );
        out.writeObject( Integer.toString( FILE_EVENT_VERSION ) );

        out.writeObject( sessionId );
        out.writeObject( nodeId );
        out.writeObject( checksum );
        out.writeObject( targetLocation );
        out.writeObject( targetPath );
        out.writeObject( eventType );
        out.writeObject( requestId );
        out.writeObject( eventVersion );
        out.writeObject( sourceLocation );
        out.writeObject( sourcePath );
        out.writeObject( md5 );
        out.writeObject( size );
        out.writeObject( sha1 );
        out.writeObject( storeKey );
    }

    @Override
    public void readExternal( ObjectInput in ) throws IOException, ClassNotFoundException
    {
        super.readExternal( in );
        int version = Integer.parseInt( ( String )in.readObject() );
        if ( version > FILE_EVENT_VERSION )
        {
            throw new IOException(
                            "Cannot deserialize. This class is of an older version: " + FILE_EVENT_VERSION +
                                            " vs. the version read from the data stream: " + version + "." );
        }
        sessionId = ( String )in.readObject();
        nodeId = ( String )in.readObject();
        checksum = ( String )in.readObject();
        targetLocation = ( String )in.readObject();
        targetPath = ( String )in.readObject();
        eventType = ( FileEventType )in.readObject();
        requestId = ( String )in.readObject();
        eventVersion = ( Integer )in.readObject();
        sourceLocation = ( String )in.readObject();
        sourcePath = ( String )in.readObject();
        md5 = ( String )in.readObject();
        size = ( Long )in.readObject();
        sha1 = ( String )in.readObject();
        storeKey = ( String )in.readObject();

    }

}
