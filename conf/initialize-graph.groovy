println "\n===============";
println "Defining labels";
println "===============\n";

mgmt = graph.openManagement();
created = false;

println "\n=============";
println "Creating keys";
println "=============\n";

// Define vertex property keys
mgmt.makePropertyKey('name').dataType(String.class).cardinality(SINGLE).make();
mgmt.makePropertyKey('age').dataType(Integer.class).cardinality(SINGLE).make();
name = mgmt.getPropertyKey('name');
age  = mgmt.getPropertyKey('age');

// Define edge property keys
mgmt.makePropertyKey('lastSeen').dataType(Date.class).cardinality(SINGLE).make();
mgmt.makePropertyKey('timesSeen').dataType(Integer.class).cardinality(SINGLE).make();
lastSeen    = mgmt.getPropertyKey('lastSeen');
timesSeen   = mgmt.getPropertyKey('timesSeen');
adjacentID  = org.janusgraph.graphdb.types.system.ImplicitKey.ADJACENT_ID;

println "\n==============";
println "Building index";
println "==============\n";

graph.tx().rollback();

// Define vertex label and build index
mgmt.makeVertexLabel('person').make();

idx0 = mgmt.buildIndex('nameIndex',Vertex.class);
idx1 = mgmt.buildIndex('ageIndex',Vertex.class);
idx0.addKey(name).buildCompositeIndex();
idx1.addKey(age).buildCompositeIndex();

    // Vertex edge label an dbuild index
mgmt.makeEdgeLabel('knows').multiplicity(SIMPLE).signature(lastSeen).make();
knows = mgmt.getEdgeLabel('knows');

mgmt.buildEdgeIndex(knows, 'knowsByLastSeen', Direction.BOTH, lastSeen);
mgmt.buildEdgeIndex(knows, 'knowsByTimesSeen', Direction.BOTH, timesSeen);
mgmt.buildEdgeIndex(knows, 'knowsByAdjacentID', Direction.BOTH, adjacentID);

println "\n==================";
println "Committing changes";
println "==================\n";
mgmt.commit();

println "\n=================================";
println "Waiting for the index to be ready";
println "=================================\n";

    // wait for vertex indexes
ManagementSystem.awaitGraphIndexStatus(graph, 'nameIndex').
        status(SchemaStatus.REGISTERED,SchemaStatus.ENABLED).call();

ManagementSystem.awaitGraphIndexStatus(graph, 'ageIndex').
        status(SchemaStatus.REGISTERED,SchemaStatus.ENABLED).call();

    // wait for edge indexes
ManagementSystem.awaitRelationIndexStatus(graph, 'knowsByLastSeen', 'knows').
    status(SchemaStatus.REGISTERED, SchemaStatus.ENABLED).call();

ManagementSystem.awaitRelationIndexStatus(graph, 'knowsByTimesSeen', 'knows').
    status(SchemaStatus.REGISTERED, SchemaStatus.ENABLED).call();

ManagementSystem.awaitRelationIndexStatus(graph, 'knowsByAdjacentID', 'knows').
    status(SchemaStatus.REGISTERED, SchemaStatus.ENABLED).call();

println "\n==========================";
println "Acquiring traversal source";
println "==========================\n";
// Setup our traversal source object
g = graph.traversal();

println "\n===============";
println "Tasks completed";
println "===============\n";
