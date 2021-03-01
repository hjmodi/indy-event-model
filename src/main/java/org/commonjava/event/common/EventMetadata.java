package org.commonjava.event.common;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class EventMetadata implements Iterable<Map.Entry<Object, Object>>, Externalizable
{
    private String packageType;
    private Map<Object, Object> metadata = new HashMap();

    public EventMetadata() {
        this.packageType = "maven";
    }

    public EventMetadata(String packageType) {
        this.packageType = packageType;
    }

    public EventMetadata(EventMetadata original) {
        this.packageType = original.packageType;
        this.metadata.putAll(original.metadata);
    }

    public Map<Object, Object> getMetadata() {
        return this.metadata;
    }

    public EventMetadata set(Object key, Object value) {
        this.metadata.put(key, value);
        return this;
    }

    public Object get(Object key) {
        return this.metadata.get(key);
    }

    public boolean containsKey(Object key) {
        return this.metadata.containsKey(key);
    }

    public boolean containsValue(Object value) {
        return this.metadata.containsValue(value);
    }

    public String getPackageType() {
        return this.packageType;
    }

    public void setPackageType(String packageType) {
        this.packageType = packageType;
    }

    public Iterator<Map.Entry<Object, Object>> iterator() {
        return this.metadata.entrySet().iterator();
    }

    public String toString() {
        return String.format("EventMetadata [metadata=%s]", this.metadata);
    }

    @Override
    public void writeExternal( ObjectOutput out ) throws IOException
    {
        out.writeObject( packageType );
        out.writeObject( metadata );
    }

    @Override
    public void readExternal( ObjectInput in ) throws IOException, ClassNotFoundException
    {
        this.packageType = (String) in.readObject();
        this.metadata = (Map<Object, Object>) in.readObject();
    }
}
