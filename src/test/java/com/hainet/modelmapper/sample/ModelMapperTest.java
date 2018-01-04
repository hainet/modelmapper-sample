package com.hainet.modelmapper.sample;

import com.hainet.modelmapper.sample.destination.Destination;
import com.hainet.modelmapper.sample.source.Source;
import org.junit.Before;
import org.junit.Test;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.convention.MatchingStrategies;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ModelMapperTest {

    private ModelMapper mapper;
    private Source source;

    @Before
    public void before() {
        mapper = new ModelMapper();

        source = new Source();

        // Flat
        source.setFlatValue(1);

        // Skipped
        source.setSkipped(true);

        // Nested
        final Source.NestedSource nestedSource = new Source.NestedSource();
        nestedSource.setFlatValue(1);
        source.setNestedSource(nestedSource);

        // Nested to flat
        final Source.User user = new Source.User();
        user.setId(1);
        user.setPassword("password");
        source.setUser(user);

        // Flat to nested
        source.setFooValue(1);
        source.setId(1);
        source.setPassword("password");
    }

    @Test
    public void flatToFlat() {
        final Destination destination = new Destination();
        mapper.map(source, destination);

        assertThat(destination.getFlatValue(), is(1));
    }

    @Test
    public void unmapped() {
        final Destination destination = new Destination();
        mapper.map(source, destination);

        assertThat(destination.isUnmapped(), is(false));
    }

    @Test
    public void skipped() {
        final PropertyMap<Source, Destination> skippedMap = new PropertyMap<Source, Destination>() {
            @Override
            protected void configure() {
                skip().setSkipped(source.isSkipped());
            }
        };
        mapper.addMappings(skippedMap);

        final Destination destination = new Destination();
        mapper.map(source, destination);

        assertThat(destination.isSkipped(), is(false));
    }

    @Test
    public void nestedToFlat() {
        final Destination destination = new Destination();
        mapper.map(source, destination);

        // CamelCaseに従っている限りはどのようなアナグラムも検出する。
        // ネストされた型 + 型のフィールド名(省略込)
        assertThat(destination.getNestedSourceFlatValue(), is(1));
        assertThat(destination.getNestedSourceFlat(), is(1));
        assertThat(destination.getNestedSourceValue(), is(1));

        // ネストされた型(省略) + 型のフィールド名(省略込)
        assertThat(destination.getNestedFlat(), is(1));
        assertThat(destination.getNestedFlat(), is(1));
        assertThat(destination.getNestedValue(), is(1));

        // 型のフィールド名 + ネストされた型 (逆順)
        assertThat(destination.getFlatValueNestedSource(), is(1));
    }

    @Test
    public void nestedToFlatByPropertyMap() {
        final PropertyMap<Source, Destination> userMap = new PropertyMap<Source, Destination>() {
            @Override
            protected void configure() {
                map().setId(source.getUser().getId());
                map().setPassword(source.getUser().getPassword());
            }
        };
        mapper.addMappings(userMap);

        final Destination destination = new Destination();
        mapper.map(source, destination);

        assertThat(destination.getId(), is(1));
        assertThat(destination.getPassword(), is("password"));
    }

    @Test
    public void nestedToFlatByLooseMatchingStrategy() {
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);

        final Destination destination = new Destination();
        mapper.map(source, destination);

        assertThat(destination.getId(), is(1));
        assertThat(destination.getPassword(), is("password"));
    }

    @Test
    public void flatToNested() {
        final Destination destination = new Destination();
        mapper.map(source, destination);

        assertThat(destination.getFoo().getValue(), is(1));
    }

    @Test
    public void flatToNestedByPropertyMap() {
        final PropertyMap<Source, Destination> userMap = new PropertyMap<Source, Destination>() {
            @Override
            protected void configure() {
                map().getUser().setId(source.getId());
                map().getUser().setPassword(source.getPassword());
            }
        };
        mapper.addMappings(userMap);

        final Destination destination = new Destination();
        mapper.map(source, destination);

        assertThat(destination.getUser().getId(), is(1));
        assertThat(destination.getUser().getPassword(), is("password"));
    }
}
