package de.rngcntr.janusbench.util;

import de.rngcntr.janusbench.backend.Connection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import org.apache.tinkerpop.gremlin.driver.Result;

/**
 * Meant to be initialized by SnakeYaml only
 */
public class BuildUpQuery {
    public String name;
    public String query;

    public BuildUpQuery() {}

    public void setName(String name) { this.name = name; }

    public void setQuery(String query) { this.query = query; }

    public String getName() { return name; }

    public Object evaluate(Connection connection, Map<String, Object> parameters) {
        if (name == null || query == null) {
            throw new IllegalStateException("Uninitialized");
        }

        List<Result> result = null;

        for (int retry = 0; retry < 3; ++retry) {
            try {
                result = connection.submit(query, parameters).all().get();
                break;
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

        if (result != null && result.size() > 0) {
            return result.get(0).getObject();
        } else {
            return null;
        }
    }
}
