package com.dz.interview.task.dto;

public class HitsCustom {

    private long hits;
    private long unique;
    private long retained;
    private HitsRange hitsRange;

    public HitsCustom(long hits, long unique, long retained, HitsRange hitsRange) {
        this.hits = hits;
        this.unique = unique;
        this.retained = retained;
        this.hitsRange = hitsRange;
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

    public long getRetained() {
        return retained;
    }

    public void setRetained(long retained) {
        this.retained = retained;
    }

    public HitsRange getHitsRange() {
        return hitsRange;
    }

    public void setHitsRange(HitsRange hitsRange) {
        this.hitsRange = hitsRange;
    }
}
