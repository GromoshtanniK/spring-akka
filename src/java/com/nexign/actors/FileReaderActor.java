package com.nexign.actors;

import akka.actor.AbstractActor;
import akka.japi.pf.ReceiveBuilder;
import com.nexign.ShardingService;
import com.nexign.configuration.akka.PropsFactory;
import com.nexign.configuration.akka.Actor;
import com.nexign.messages.CDRData;
import com.nexign.messages.Idle;
import com.nexign.messages.ProduceSignal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static java.nio.file.StandardCopyOption.ATOMIC_MOVE;

@Actor
public class FileReaderActor extends AbstractActor {

    private final PropsFactory propsFactory;
    private ShardingService shardingService;
    private Long shard;

    @Value("${read.directory}")
    private String readDirectory;

    @Value("${waste.directory}")
    private String wasteDirectory;


    @Autowired
    public FileReaderActor(PropsFactory propsFactory, ShardingService shardingService) {
        this.propsFactory = propsFactory;
        this.shardingService = shardingService;
        shard = shardingService.reserveShardNumber();
    }


    @Override
    public Receive createReceive() {
        return ReceiveBuilder
                .create().match(ProduceSignal.class, produceSignal -> {
                    System.out.println("READ_MESSAGE");
                    Optional<Path> path = Files.list(Paths.get(readDirectory)).findFirst();
                    if (path.isPresent()) {
                        byte[] fileData = Files.readAllBytes(path.get());
                        Files.move(path.get(), Paths.get(wasteDirectory + "/" + path.get().getFileName()), ATOMIC_MOVE);
                        getSender().tell(new CDRData(fileData), getContext().getParent());
                        System.out.println("DONE READ");
                    } else {
                        getSender().tell(new Idle(), getContext().getParent());
                    }
                })
                .build();
    }
}
