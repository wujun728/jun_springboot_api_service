package com.yuyaogc.lowcode.engine.util;

import java.net.InetAddress;

public class DefaultIdentifierGenerator implements IdentifierGenerator {
    private final Sequence sequence;

    public DefaultIdentifierGenerator() {
        this.sequence = new Sequence(null);
    }

    public DefaultIdentifierGenerator(InetAddress inetAddress) {
        this.sequence = new Sequence(inetAddress);
    }

    public DefaultIdentifierGenerator(long workerId, long dataCenterId) {
        this.sequence = new Sequence(workerId, dataCenterId);
    }

    public DefaultIdentifierGenerator(Sequence sequence) {
        this.sequence = sequence;
    }

    @Override
    public Long nextId(Object entity) {
        return sequence.nextId();
    }
}
