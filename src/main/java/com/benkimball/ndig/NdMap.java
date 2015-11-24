package com.benkimball.ndig;

import org.neo4j.graphdb.*;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.schema.ConstraintDefinition;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class NdMap {

    private static final GraphDatabaseService gdb =
            new GraphDatabaseFactory().newEmbeddedDatabase(new File("target/graph.db"));
    public static NdRoom home = initializeGraph();

    private static NdRoom initializeGraph() {
        Node home_node;
        NdRoom home = null;

        registerShutdownHook();

        try(Transaction tx = gdb.beginTx()) {
            gdb.schema().getConstraints(DynamicLabel.label("Room")).forEach(ConstraintDefinition::drop);
            tx.success();
        }
        // find or create home (root) node
        try(Transaction tx = gdb.beginTx()) {
            String query = "MERGE (n:Room {name: {name}, description: {description}}) RETURN n";
            Map<String, Object> params = new HashMap<>();
            params.put("name", "Home");
            params.put("description", "This comfortable room has lots of rugs and pillows.");
            ResourceIterator<Node> resultIterator = gdb.execute(query, params).columnAs("n");
            home_node = resultIterator.next();
            tx.success();
        }
        if(home_node != null) {
            home = new NdRoom(home_node);
        }
        return home;
    }

    private static void registerShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                gdb.shutdown();
            }
        });
    }
}
