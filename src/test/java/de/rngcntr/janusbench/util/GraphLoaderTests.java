package de.rngcntr.janusbench.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class GraphLoaderTests {

    @ParameterizedTest
    @MethodSource("createInputFiles")
    public void testGraphLoaderConstructor(String extension, boolean exists, boolean readable,
                                           boolean shouldThrowException) {
        File mockedInputFile = mock(File.class);
        when(mockedInputFile.getName()).thenReturn("myMockedInputFile." + extension);
        when(mockedInputFile.exists()).thenReturn(exists);
        when(mockedInputFile.canRead()).thenReturn(readable);

        if (shouldThrowException) {
            assertThrows(IllegalArgumentException.class, () -> new GraphLoader(mockedInputFile));
        } else {
            GraphLoader gl = new GraphLoader(mockedInputFile);
            assertEquals(extension, gl.getFileExtension());
        }
    }

    private static Stream<Arguments> createInputFiles() {
        return Stream.of(
            Arguments.of(".json", true, true, false),
            Arguments.of(".json", true, false, true),
            Arguments.of(".json", false, true, true),
            Arguments.of(".json", false, false, true),
            Arguments.of(".graphml", true, true, false),
            Arguments.of(".graphml", true, false, true),
            Arguments.of(".graphml", false, true, true),
            Arguments.of(".graphml", false, false, true),
            Arguments.of(".kryo", true, true, false),
            Arguments.of(".kryo", true, false, true),
            Arguments.of(".kryo", false, true, true),
            Arguments.of(".kryo", false, false, true),
            Arguments.of(".invalidExtension", true, true, true),
            Arguments.of(".invalidExtension", true, false, true),
            Arguments.of(".invalidExtension", false, true, true),
            Arguments.of(".invalidExtension", false, false, true)
        );
    }
}