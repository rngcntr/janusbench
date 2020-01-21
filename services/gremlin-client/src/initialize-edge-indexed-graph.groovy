println "\n===============";[]
println "Defining labels";[]
println "===============\n";[]

mgmt = graph.openManagement()

println "\n=============";[]
println "Creating keys";[]
println "=============\n";[]
;[]// Define vertex property keys
mgmt.makePropertyKey('name').dataType(String.class).cardinality(SINGLE).make()
mgmt.makePropertyKey('age').dataType(Integer.class).cardinality(SINGLE).make()

;[]// Define edge property keys
mgmt.makePropertyKey('lastSeen').dataType(Date.class).cardinality(SINGLE).make()
mgmt.makePropertyKey('inVertexID').dataType(Integer.class).cardinality(SINGLE).make()
mgmt.makePropertyKey('outVertexID').dataType(Integer.class).cardinality(SINGLE).make()

println "\n==============";[]
println "Building index";[]
println "==============\n";[]

;[]// Construct a composite index for a few commonly used property keys
graph.tx().rollback();[]

name  = mgmt.getPropertyKey('name')
age   = mgmt.getPropertyKey('age')

;[]// Define vertex labels
mgmt.makeVertexLabel('person').make()

idx0 = mgmt.buildIndex('nameIndex',Vertex.class)
idx1 = mgmt.buildIndex('ageIndex',Vertex.class)

idx0.addKey(name).buildCompositeIndex()
idx1.addKey(age).buildCompositeIndex()

;[] // Define Edge Labels
lastSeen = mgmt.getPropertyKey('lastSeen')
inVertexID  = mgmt.getPropertyKey('inVertexID')
outVertexID = mgmt.getPropertyKey('outVertexID')

mgmt.makeEdgeLabel('knows').multiplicity(SIMPLE).signature(lastSeen, inVertexID, outVertexID).make()
knows = mgmt.getEdgeLabel('knows')

;[] // Vertex Centric Indices
//mgmt.buildEdgeIndex(knows, 'knowsByInID', Direction.BOTH, Order.decr, inVertexID)
//mgmt.buildEdgeIndex(knows, 'knowsByOutID', Direction.BOTH, Order.decr, outVertexID)
mgmt.buildEdgeIndex(knows, 'knowsByAdjacentID', Direction.BOTH, Order.decr, org.janusgraph.graphdb.types.system.ImplicitKey.ACJACENT_ID)

println "\n==================";[]
println "Committing changes";[]
println "==================\n";[]
mgmt.commit()


println "\n=================================";[]
println "Waiting for the index to be ready";[]
println "=================================\n";[]

mgmt.awaitGraphIndexStatus(graph, 'nameIndex').
     status(SchemaStatus.REGISTERED,SchemaStatus.ENABLED).call()

mgmt.awaitGraphIndexStatus(graph, 'ageIndex').
     status(SchemaStatus.REGISTERED,SchemaStatus.ENABLED).call()

//mgmt.awaitRelationIndexStatus(graph, 'knowsByInID', 'knows').
    //status(SchemaStatus.REGISTERED, SchemaStatus.ENABLED).call()

//mgmt.awaitRelationIndexStatus(graph, 'knowsByOutID', 'knows').
    //status(SchemaStatus.REGISTERED, SchemaStatus.ENABLED).call()

mgmt.awaitRelationIndexStatus(graph, 'knowsByAdjacentID', 'knows').
    status(SchemaStatus.REGISTERED, SchemaStatus.ENABLED).call()

println "\n==========================";[]
println "Acquiring traversal source";[]
println "==========================\n";[]
;[]// Setup our traversal source object
g = graph.traversal()

println "\n===============";[]
println "Tasks completed";[]
println "===============\n";[]
