package com.benkimball.ndig;

import org.neo4j.graphdb.*;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

public class NdMap {

    private static final GraphDatabaseService gdb;

    static {
        gdb = new GraphDatabaseFactory().newEmbeddedDatabase(new File("target/graph.db"));
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                gdb.shutdown();
            }
        });
    }

    private ConcurrentSkipListMap<Number,NdRoom> rooms;

    public NdMap() {
        rooms = new ConcurrentSkipListMap<>();
        rooms.put(0, findOrCreateDefaultRoom());
    }

    public NdRoom getDefaultRoom() {
        return getRoom(0);
    }

    public NdRoom getRoom(Number id) {
        return rooms.get(id);
    }

    private NdRoom findOrCreateDefaultRoom() {
        Node home_node;
        try(Transaction tx = gdb.beginTx()) {
            String query = "MERGE (n:Room {id: {id}}) ON CREATE SET n.name={name}, n.description={desc} RETURN n";
            Map<String,Object> params = new HashMap<>();
            params.put("id", 0);
            params.put("name", "Home");
            params.put("desc", "This comfortable room has lots of rugs and pillows.");
            ResourceIterator<Node> iter = gdb.execute(query, params).columnAs("n");
            home_node = iter.next();
            iter.close();
            tx.success();
        }
        return new NdRoom(home_node);
    }
}
