package de.rngcntr.janusbench.util;

import de.rngcntr.janusbench.backend.Connection;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;
import picocli.CommandLine.ITypeConverter;
import picocli.CommandLine.TypeConversionException;

public class GraphLoader {
    private static final String RESOURCE_LOCATION = "/tmp/janusbench";
    private static final String RESOURCE_NAME = "default-graph";

    private final File inputFile;
    private final FileType fileType;

    public GraphLoader(File inputFile) {
        if (!inputFile.exists() || !inputFile.canRead()) {
            throw new IllegalArgumentException(
                String.format("Graph resource %s does not exist or is not readable",
                              inputFile.getAbsolutePath()));
        }
        this.inputFile = inputFile;
        this.fileType = FileType.byExtension(inputFile);
    }

    public String getFileExtension() { return fileType.fileExtension; }

    public void loadGraph(Connection connection) throws IOException {
        // make temp directory /tmp/janusbench
        File outputDirectory = new File(RESOURCE_LOCATION);
        outputDirectory.mkdirs();
        
        // copy input file to /tmp/janusbench/default-graph
        File outputFile = new File(RESOURCE_LOCATION + File.separator + RESOURCE_NAME);
        Files.copy(inputFile.toPath(), outputFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

        // load graph into database
        connection.submit(fileType.getLoadRequest());
    }

    public static class CompletionCandidates extends ArrayList<String> {
        private static final long serialVersionUID = 1L;

        public CompletionCandidates() {
            super(Arrays.asList(FileType.values())
                      .stream()
                      .map(t -> t.fileExtension)
                      .collect(Collectors.toList()));
        }
    }

    private static enum FileType {
        GRAPHML(".graphml", "IoCore.graphml()"),
        GRAPHSON(".json", "IoCore.graphson()"),
        KRYO(".kryo", "IoCore.gryo()");

        private static String loadRequest = "graph.io(%s).readGraph(\"%s\")\n"
                                            + "graph.tx().commit()";

        private final String fileExtension;
        private final String ioCoreBuilder;

        FileType(final String fileExtension, String ioCoreBuilder) {
            this.fileExtension = fileExtension;
            this.ioCoreBuilder = ioCoreBuilder;
        }

        public static FileType byExtension(File inputFile) {
            String extension = inputFile.getName().substring(inputFile.getName().lastIndexOf("."));
            for (FileType fileType : FileType.values()) {
                if (fileType.fileExtension.equals(extension)) {
                    return fileType;
                }
            }

            throw new IllegalArgumentException(
                String.format("File type \"%s\" can not be used as graph resource.", extension));
        }

        public String getLoadRequest() {
            // resource has to be copied to the /tmp/janusbench/ directory on the JanusGraph Host
            // without file extension
            return String.format(loadRequest, ioCoreBuilder, RESOURCE_LOCATION + File.separator + RESOURCE_NAME);
        }
    }

    public static class GraphLoaderConverter implements ITypeConverter<GraphLoader> {
        @Override
        public GraphLoader convert(String fileName) throws Exception {
            try {
                GraphLoader gl = new GraphLoader(new File(fileName));
                return gl;
            } catch (IllegalArgumentException iaex) {
                throw new TypeConversionException(String.format(
                    "Graph resource File %s is unavailable or has invalid file type", fileName));
            }
        }
    }
}