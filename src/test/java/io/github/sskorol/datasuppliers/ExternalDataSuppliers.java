package io.github.sskorol.datasuppliers;

import io.github.sskorol.dataprovider.DataSupplier;
import io.github.sskorol.model.User;
import org.testng.annotations.DataProvider;

import java.util.Iterator;
import java.util.List;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import static java.util.Arrays.asList;

public class ExternalDataSuppliers {

    @DataSupplier(extractValues = true)
    public User[] getExternalArrayData() {
        return new User[]{
                new User("user1", "password1"),
                new User("user2", "password2")
        };
    }

    @DataSupplier
    public List<String> getExternalCollectionData() {
        return asList("data1", "data2");
    }

    @DataSupplier
    public Stream<Long> getExternalStreamData() {
        return LongStream.range(0, 10)
                         .filter(i -> i % 2 != 0)
                         .boxed();
    }

    @DataSupplier
    public double getExternalObjectData() {
        return 0.1;
    }

    @DataProvider
    public static Iterator<Object[]> getCommonData() {
        return Stream.of("data").map(d -> new Object[]{d}).iterator();
    }
}
