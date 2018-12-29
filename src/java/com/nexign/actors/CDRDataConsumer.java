package com.nexign.actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Scheduler;
import akka.japi.pf.ReceiveBuilder;
import akka.routing.SmallestMailboxPool;
import com.nexign.configuration.akka.PropsFactory;
import com.nexign.configuration.akka.PrototypeActor;
import com.nexign.messages.CDRData;
import com.nexign.messages.Idle;
import com.nexign.messages.ProduceSignal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.time.Duration;

@PrototypeActor
public class CDRDataConsumer extends AbstractActor {

    @Value("${file.readers.size}")
    private int fileReadersSize;

    @Value("${file.parsers.size}")
    private int fileParsersSize;

    private ActorRef fileReadersRouter;

    private ActorRef fileParsersRouter;

    private final PropsFactory propsFactory;

    @Autowired
    public CDRDataConsumer(PropsFactory propsFactory) {
        this.propsFactory = propsFactory;
    }

    @Override
    public void preStart() throws Exception {
        fileReadersRouter = getContext()
                .actorOf(
                        new SmallestMailboxPool(fileReadersSize).props(propsFactory.props(FileReaderActor.class)),
                        "file_readers_router");

        fileParsersRouter = getContext()
                .actorOf(
                        new SmallestMailboxPool(fileParsersSize).props(propsFactory.props(FileParser.class)),
                        "file_parsers_router");


        fileReadersRouter.tell(new ProduceSignal(), getSelf());
    }

    @Override
    public Receive createReceive() {
        return ReceiveBuilder
                .create()
                .match(CDRData.class, cdrData -> {
                    fileParsersRouter.tell(cdrData, getSelf());
                    fileReadersRouter.tell(new ProduceSignal(), getSelf());
                })
                .match(Idle.class, idle -> {
                    Scheduler scheduler = getContext()
                            .getSystem()
                            .scheduler();
                    scheduler.scheduleOnce(Duration.ofMillis(1000L), fileReadersRouter, new ProduceSignal(), getContext().dispatcher(), getSelf());
                })
                .build();
    }
}
