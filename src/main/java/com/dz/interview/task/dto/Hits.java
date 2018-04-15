package com.dz.interview.task.dto;

public class Hits {

    private long hits;
    private long unique;

    public Hits() {
    }

    public Hits(long hits, long unique) {
        this.hits = hits;
        this.unique = unique;
    }

    public long getHits() {
        return hits;
    }

    public void setHits(long hits) {
        this.hits = hits;
    }

    public long getUnique() {
        return unique;
    }

    public void setUnique(long unique) {
        this.unique = unique;
    }
}
