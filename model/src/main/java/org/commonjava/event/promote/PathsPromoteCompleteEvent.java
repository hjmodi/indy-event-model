/**
 * Copyright (C) 2022-2023 Red Hat, Inc. (https://github.com/Commonjava/indy-event-model)
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

import org.commonjava.event.common.IndyEvent;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Set;

public class PathsPromoteCompleteEvent
                extends IndyEvent
                implements Externalizable
{
    private static final int PATHS_PROMOTE_COMPLETE_EVENT_VERSION = 1;

    private String promotionId;

    private String sourceStore;

    private String targetStore;

    private Set<String> completedPaths;

    private Set<String> skippedPaths;

    private boolean purgeSource;

    public PathsPromoteCompleteEvent()
    {
    }

    public PathsPromoteCompleteEvent( String promotionId, String sourceStore, String targetStore,
                                      Set<String> completedPaths, Set<String> skippedPaths, boolean purgeSource )
    {
        this.promotionId = promotionId;
        this.sourceStore = sourceStore;
        this.targetStore = targetStore;
        this.completedPaths = completedPaths;
        this.skippedPaths = skippedPaths;
        this.purgeSource = purgeSource;
    }

    public String getPromotionId()
    {
        return promotionId;
    }

    public void setPromotionId( String promotionId )
    {
        this.promotionId = promotionId;
    }

    public String getSourceStore()
    {
        return sourceStore;
    }

    public void setSourceStore( String sourceStore )
    {
        this.sourceStore = sourceStore;
    }

    public String getTargetStore()
    {
        return targetStore;
    }

    public void setTargetStore( String targetStore )
    {
        this.targetStore = targetStore;
    }

    public Set<String> getCompletedPaths()
    {
        return completedPaths;
    }

    public void setCompletedPaths( Set<String> completedPaths )
    {
        this.completedPaths = completedPaths;
    }

    public boolean isPurgeSource()
    {
        return purgeSource;
    }

    public void setPurgeSource( boolean purgeSource )
    {
        this.purgeSource = purgeSource;
    }

    public Set<String> getSkippedPaths()
    {
        return skippedPaths;
    }

    public void setSkippedPaths( Set<String> skippedPaths )
    {
        this.skippedPaths = skippedPaths;
    }

    @Override
    public void writeExternal( ObjectOutput out ) throws IOException
    {
        super.writeExternal( out );
        out.writeObject( Integer.toString( PATHS_PROMOTE_COMPLETE_EVENT_VERSION ) );

        out.writeObject( promotionId );
        out.writeObject( sourceStore );
        out.writeObject( targetStore );
        out.writeObject( completedPaths );
        out.writeObject( skippedPaths );
        out.writeObject( purgeSource );
    }

    @Override
    public void readExternal( ObjectInput in ) throws IOException, ClassNotFoundException
    {
        super.readExternal( in );
        int version = Integer.parseInt( (String) in.readObject() );
        if ( version > PATHS_PROMOTE_COMPLETE_EVENT_VERSION )
        {
            throw new IOException( "Cannot deserialize. This class is of an older version: "
                                                   + PATHS_PROMOTE_COMPLETE_EVENT_VERSION
                                                   + " vs. the version read from the data stream: " + version + "." );
        }
        promotionId = (String) in.readObject();
        sourceStore = (String) in.readObject();
        targetStore = (String) in.readObject();
        completedPaths = (Set<String>) in.readObject();
        skippedPaths = (Set<String>) in.readObject();
        purgeSource = (boolean) in.readObject();
    }

    @Override
    public String toString()
    {
        return "PathsPromoteCompleteEvent{" + "promotionId='" + promotionId + '\'' + ", sourceStore='" + sourceStore
                        + '\'' + ", targetStore='" + targetStore + '\'' + ", completedPaths=" + completedPaths
                        + ", skippedPaths=" + skippedPaths + ", purgeSource=" + purgeSource + '}';
    }
}
