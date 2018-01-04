package com.hainet.modelmapper.sample.destination;

import lombok.Data;

@Data
public class Destination {

    // Flat to flat
    private int flatValue;

    // Unmapped
    private boolean unmapped;

    // Skipped
    private boolean skipped;

    // Nested to flat
    // Name strict
    private int nestedSourceFlatValue;
    private int nestedSourceFlat;
    private int nestedSourceValue;

    private int nestedFlatValue;
    private int nestedFlat;
    private int nestedValue;

    private int flatValueNestedSource;

    // Name loose
    private int id;
    private String password;

    // Flat to nested
    // Name strict
    @Data
    public static class Foo {

        private int value;
    }

    private Foo foo;

    // Name loose
    @Data
    public static class User {

        private int id;

        private String password;
    }

    private User user;
}
