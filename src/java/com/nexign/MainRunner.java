package com.nexign;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import com.nexign.actors.CDRDataConsumer;
import com.nexign.actors.InitActor;
import com.nexign.configuration.akka.PropsFactory;
import com.nexign.messages.SomeMessage;
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
        ActorRef actorRef = actorSystem.actorOf(extension.props(CDRDataConsumer.class), "cdr_consumer");
//        actorRef.tell(new SomeMessage(), null);
    }
}