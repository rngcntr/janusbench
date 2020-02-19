package de.rngcntr.janusbench.util;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import de.rngcntr.janusbench.backend.Connection;
import org.junit.jupiter.api.Test;

public class ComposedBenchmarkTests {

    @Test
    public void testRunSingle() {
        Connection mockedConnection = mock(Connection.class);
        ComposedBenchmark cb = new ComposedBenchmark(mockedConnection);

        Benchmark b0 = mock(Benchmark.class);

        cb.addComponent(b0);
        cb.run();

        verify(b0, times(1)).run();
    }

    @Test
    public void testRunSingleTenTimes() {
        Connection mockedConnection = mock(Connection.class);
        ComposedBenchmark cb = new ComposedBenchmark(mockedConnection);

        Benchmark b0 = mock(Benchmark.class);

        cb.addComponent(b0, 10);
        cb.run();

        verify(b0, times(10)).run();
    }

    @Test
    public void testRunMultiple(){
        Connection mockedConnection = mock(Connection.class);
        ComposedBenchmark cb = new ComposedBenchmark(mockedConnection);

        Benchmark b0 = mock(Benchmark.class);
        Benchmark b1 = mock(Benchmark.class);

        cb.addComponent(b0);
        cb.addComponent(b1);
        cb.run();

        verify(b0, times(1)).run();
        verify(b1, times(1)).run();
    }
}