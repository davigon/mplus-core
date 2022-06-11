package com.github.davigon.domain;

import lombok.Getter;

@Getter
public class Channel {
    private final String id;

    private final String name;

    private final int number;

    private final String logoUrl;

    public Channel(String id, String name, int number, String logoUrl) {
        this.id = id;
        this.name = name;
        this.number = number;
        this.logoUrl = logoUrl;
    }
}
