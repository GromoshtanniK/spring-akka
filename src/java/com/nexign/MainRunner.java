package com.nexign;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.routing.SmallestMailboxPool;
import com.nexign.actors.CDRDataConsumer;
import com.nexign.actors.CassandraAccessActor;
import com.nexign.actors.InitActor;
import com.nexign.actors.SimpleSaveActor;
import com.nexign.actors.exception.SuperVisor;
import com.nexign.configuration.akka.PropsFactory;
import com.nexign.messages.SomeMessage;
import com.nexign.messages.TickSignal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

@Service
public class MainRunner implements CommandLineRunner {

    private final ActorSystem actorSystem;
    private final PropsFactory extension;

    @Autowired
    public MainRunner(ActorSystem actorSystem, PropsFactory extension) {
        this.actorSystem = actorSystem;
        this.extension = extension;
    }

    @Override
    public void run(String... args) throws Exception {
//        ActorRef actorRef = actorSystem.actorOf(new SmallestMailboxPool(1000).props(extension.props(SimpleSaveActor.class)), "simple_save");
        ActorRef actorRef = actorSystem.actorOf(extension.props(SuperVisor.class), "supervisor");
        actorRef.tell(new TickSignal(), null);
//        actorRef.tell(new SomeMessage(), null);
//        actorRef.tell(new SomeMessage(), null);
    }
}