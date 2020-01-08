:remote connect tinkerpop.server conf/remote.yaml
:remote console

println "\n===============";[]
println "Defining labels";[]
println "===============\n";[]
;[]// Define edge labels and usage
mgmt = graph.openManagement()
mgmt.makeEdgeLabel('follows').multiplicity(SIMPLE).make()

;[]// Define vertex labels
mgmt.makeVertexLabel('person').make()

println "\n=============";[]
println "Creating keys";[]
println "=============\n";[]
;[]// Define vertex property keys
mgmt.makePropertyKey('name').dataType(String.class).cardinality(SINGLE).make()
mgmt.makePropertyKey('age').dataType(Integer.class).cardinality(SINGLE).make()

;[]// Define edge property keys
mgmt.makePropertyKey('since').dataType(Date.class).cardinality(SINGLE).make()

println "\n==============";[]
println "Building index";[]
println "==============\n";[]

;[]// Construct a composite index for a few commonly used property keys
graph.tx().rollback();[]

idx0 = mgmt.buildIndex('nameIndex',Vertex.class)
idx1 = mgmt.buildIndex('ageIndex',Vertex.class)
idx2 = mgmt.buildIndex('sinceIndex',Vertex.class)

name  = mgmt.getPropertyKey('name')
age   = mgmt.getPropertyKey('age')
since = mgmt.getPropertyKey('since')

idx0.addKey(name).buildCompositeIndex()
idx1.addKey(age).buildCompositeIndex()
idx2.addKey(since).buildCompositeIndex()

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

mgmt.awaitGraphIndexStatus(graph, 'sinceIndex').
     status(SchemaStatus.REGISTERED,SchemaStatus.ENABLED).call()

println "\n==========================";[]
println "Acquiring traversal source";[]
println "==========================\n";[]
;[]// Setup our traversal source object
g = graph.traversal()

println "\n===============";[]
println "Tasks completed";[]
println "===============\n";[]
