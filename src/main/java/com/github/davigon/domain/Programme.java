package com.github.davigon.domain;

import lombok.Getter;

import java.time.OffsetDateTime;

@Getter
public class Programme {
    private final OffsetDateTime start;

    private OffsetDateTime stop;

    private final String channel;

    private final String title;

    private final String description;

    private final String category;

    public Programme(OffsetDateTime start, OffsetDateTime stop, String channel, String title, String description, String category) {
        this.start = start;
        this.stop = stop;
        this.channel = channel;
        this.title = title;
        this.description = description;
        this.category = category;
    }

    public void setStop(OffsetDateTime stop) {
        this.stop = stop;
    }
}
