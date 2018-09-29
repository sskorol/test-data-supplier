package io.github.sskorol.datasuppliers;

import io.github.sskorol.core.DataSupplier;
import io.github.sskorol.entities.User;
import io.vavr.Tuple;
import one.util.streamex.StreamEx;
import org.testng.annotations.DataProvider;

import java.util.Iterator;
import java.util.List;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import static java.util.Arrays.asList;

public class ExternalDataSuppliers {

    @DataSupplier(transpose = true)
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

    @DataSupplier(name = "Password supplier")
    public String getPasswordFromNamedDataSupplier() {
        return "qwerty";
    }

    @DataSupplier(transpose = true)
    public Tuple getExternalTupleData() {
        return Tuple.of("1", 2, 3.0);
    }

    @DataProvider
    public static Iterator<Object[]> getCommonData() {
        return Stream.of("data").map(d -> new Object[]{d}).iterator();
    }

    @DataSupplier
    public String getClassLevelGlobalData() {
        return "data1";
    }

    @DataSupplier
    public String getClassLevelLocalData() {
        return "data2";
    }

    @DataSupplier(flatMap = true)
    public StreamEx externalHashes() {
        return StreamEx.of(Tuple.of("hash1", "password1"), Tuple.of("hash2", "password2"));
    }
}
