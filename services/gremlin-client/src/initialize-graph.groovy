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
name = mgmt.getPropertyKey('name')
age  = mgmt.getPropertyKey('age')

;[]// Define edge property keys
mgmt.makePropertyKey('lastSeen').dataType(Date.class).cardinality(SINGLE).make()
mgmt.makePropertyKey('inVertexID').dataType(Integer.class).cardinality(SINGLE).make() // TODO: delete
mgmt.makePropertyKey('outVertexID').dataType(Integer.class).cardinality(SINGLE).make() // TODO: delete
lastSeen    = mgmt.getPropertyKey('lastSeen')
inVertexID  = mgmt.getPropertyKey('inVertexID')
outVertexID = mgmt.getPropertyKey('outVertexID')

println "\n==============";[]
println "Building index";[]
println "==============\n";[]

graph.tx().rollback();[]

;[]// Define vertex label and build index
mgmt.makeVertexLabel('person').make()

idx0 = mgmt.buildIndex('nameIndex',Vertex.class)
idx1 = mgmt.buildIndex('ageIndex',Vertex.class)
idx0.addKey(name).buildCompositeIndex()
idx1.addKey(age).buildCompositeIndex()

;[] // Vertex edge label an dbuild index
mgmt.makeEdgeLabel('knows').multiplicity(SIMPLE).signature(lastSeen).make()
knows = mgmt.getEdgeLabel('knows')

mgmt.buildEdgeIndex(knows, 'knowsByInID', Direction.BOTH, Order.decr, inVertexID)
mgmt.buildEdgeIndex(knows, 'knowsByOutID', Direction.BOTH, Order.decr, outVertexID)

println "\n==================";[]
println "Committing changes";[]
println "==================\n";[]
mgmt.commit()

println "\n=================================";[]
println "Waiting for the index to be ready";[]
println "=================================\n";[]

;[] // wait for vertex indexes
mgmt.awaitGraphIndexStatus(graph, 'nameIndex').
     status(SchemaStatus.REGISTERED,SchemaStatus.ENABLED).call()

mgmt.awaitGraphIndexStatus(graph, 'ageIndex').
     status(SchemaStatus.REGISTERED,SchemaStatus.ENABLED).call()

;[] // wait for edge indexes
mgmt.awaitRelationIndexStatus(graph, 'knowsByInID', 'knows').
    status(SchemaStatus.REGISTERED, SchemaStatus.ENABLED).call()

mgmt.awaitRelationIndexStatus(graph, 'knowsByOutID', 'knows').
    status(SchemaStatus.REGISTERED, SchemaStatus.ENABLED).call()

println "\n==========================";[]
println "Acquiring traversal source";[]
println "==========================\n";[]
;[]// Setup our traversal source object
g = graph.traversal()

println "\n===============";[]
println "Tasks completed";[]
println "===============\n";[]
