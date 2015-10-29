package com.benkimball.ndig;

import java.net.InetSocketAddress;

public class NdPlayer {
    private InetSocketAddress addr;
    private String name;
    private Number line_number;

    public NdPlayer(InetSocketAddress addr, Number line_number) {
        this.addr = addr;
        this.name = "guest" + addr.getPort();
        this.line_number = line_number;
    }

    public String getName() {
        return name;
    }
    public Number getLineNumber() { return line_number; }
}
