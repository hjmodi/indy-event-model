package org.commonjava.event.promote;

import java.util.Set;

public class PathsPromoteCompleteEvent {

    private String sourceStore;

    private String targetStore;

    private Set<String> completedPaths;

    public PathsPromoteCompleteEvent() {
    }

    public PathsPromoteCompleteEvent(String sourceStore, String targetStore, Set<String> completedPaths) {
        this.sourceStore = sourceStore;
        this.targetStore = targetStore;
        this.completedPaths = completedPaths;
    }

    public String getSourceStore() {
        return sourceStore;
    }

    public void setSourceStore(String sourceStore) {
        this.sourceStore = sourceStore;
    }

    public String getTargetStore() {
        return targetStore;
    }

    public void setTargetStore(String targetStore) {
        this.targetStore = targetStore;
    }

    public Set<String> getCompletedPaths() {
        return completedPaths;
    }

    public void setCompletedPaths(Set<String> completedPaths) {
        this.completedPaths = completedPaths;
    }

    @Override
    public String toString() {
        return "PathsPromoteCompleteEvent{" +
                "sourceStore='" + sourceStore + '\'' +
                ", targetStore='" + targetStore + '\'' +
                ", completedPaths=" + completedPaths +
                '}';
    }
}
