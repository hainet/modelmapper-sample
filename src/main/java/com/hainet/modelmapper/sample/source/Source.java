package com.hainet.modelmapper.sample.source;

import lombok.Data;

@Data
public class Source {

    // Flat to flat
    private int flatValue;

    // Nested to flat
    // Name strict
    @Data
    public static class NestedSource {

        private int flatValue;
    }

    private NestedSource nestedSource;

    // Name loose
    @Data
    public static class User {

        private int id;

        private String password;
    }

    private User user;

    // Flat to nested
    // Name strict
    private int fooValue;

    // Name loose
    private int id;
    private String password;
}
