package com.benkimball.ndig;

import java.util.concurrent.CopyOnWriteArraySet;
import java.util.stream.Stream;

public class NdPlayerSet extends CopyOnWriteArraySet<NdPlayer> {

    public Stream<NdPlayer> ignore(NdPlayer speaker) {
        return stream().filter(p -> !p.isIgnoring(speaker));
    }

}
