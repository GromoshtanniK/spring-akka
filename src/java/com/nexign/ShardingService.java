package com.nexign;

import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicLong;

@Service
public class ShardingService {

    private AtomicLong atomicLong = new AtomicLong(0);

    public long reserveShardNumber() {
        return atomicLong.getAndIncrement();
    }

    public long numberOfShards() {
        return atomicLong.get();
    }
}
