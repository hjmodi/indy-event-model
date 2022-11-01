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
package org.commonjava.event.store;

import com.fasterxml.jackson.annotation.JsonIgnore;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class EventStoreKey
{
    @JsonIgnore
    private String packageType;

    @JsonIgnore
    private String storeType;

    @JsonIgnore
    private String storeName;

    protected EventStoreKey()
    {
    }

    public EventStoreKey( final String packageType, final String storeType, final String storeName )
    {
        this.packageType = packageType;
        this.storeType = storeType;
        this.storeName = storeName;
    }

    public String getPackageType()
    {
        return packageType;
    }

    public void setPackageType( String packageType )
    {
        this.packageType = packageType;
    }

    public String getStoreType()
    {
        return storeType;
    }

    public void setStoreType( String storeType )
    {
        this.storeType = storeType;
    }

    public String getStoreName()
    {
        return storeName;
    }

    public void setStoreName( String storeName )
    {
        this.storeName = storeName;
    }

    public EventStoreKey copyOf()
    {
        return new EventStoreKey( this.packageType, this.storeType, this.storeName );
    }

    public static EventStoreKey fromString( final String id )
    {
        String[] parts = id.split( ":" );

        String packageType = null;
        String name;
        String type = null;

        // FIXME: We need to get to a point where it's safe for this to be an error and not default to maven.
        if ( parts.length < 2 )
        {
            packageType = "maven";
            type = "remote";
            name = id;
        }
        else if ( parts.length < 3 || isBlank( parts[0] ) )
        {
            packageType = "maven";
            type = parts[0];
            name = parts[1];
        }
        else
        {
            packageType = parts[0];
            type = parts[1];
            name = parts[2];
        }

        if ( type == null )
        {
            throw new IllegalArgumentException( "Invalid StoreType: " + parts[1] );
        }

        return new EventStoreKey( packageType, type, name );
    }

    @Override
    public final int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ( ( packageType == null ) ? 7 : packageType.hashCode() );
        result = prime * result + ( ( storeName == null ) ? 13 : storeName.hashCode() );
        result = prime * result + ( ( storeType == null ) ? 17 : storeType.hashCode() );
        return result;
    }

    @Override
    public final boolean equals( final Object obj )
    {
        if ( this == obj )
        {
            return true;
        }
        if ( obj == null )
        {
            return false;
        }
        if ( getClass() != obj.getClass() )
        {
            return false;
        }
        final EventStoreKey other = (EventStoreKey) obj;
        if ( packageType == null )
        {
            if ( other.packageType != null )
            {
                return false;
            }
        }
        else if ( !packageType.equals( other.packageType ) )
        {
            return false;
        }
        if ( storeName == null )
        {
            if ( other.storeName != null )
            {
                return false;
            }
        }
        else if ( !storeName.equals( other.storeName ) )
        {
            return false;
        }
        if ( storeType == null )
        {
            return other.storeType == null;
        }
        return storeType.equals( other.storeType );
    }

    @Override
    public String toString()
    {
        return String.format( "%s:%s:%s", packageType, storeType, storeName );
    }
}
