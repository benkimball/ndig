package com.benkimball.ndig;

import java.net.InetSocketAddress;

public class NdPlayer {
    private InetSocketAddress addr;
    private String name;

    public NdPlayer(InetSocketAddress addr) {
        this.addr = addr;
        this.name = "guest" + addr.getPort();
    }

    public String getName() {
        return name;
    }
}
