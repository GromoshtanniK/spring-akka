package com.nexign.actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.japi.pf.ReceiveBuilder;
import akka.routing.RandomPool;
import com.nexign.configuration.akka.PrototypeActor;
import com.nexign.configuration.akka.PropsFactory;
import com.nexign.messages.SomeMessage;
import org.springframework.beans.factory.annotation.Autowired;

@PrototypeActor
public class InitActor extends AbstractActor {

    private final PropsFactory propsFactory;
    private ActorRef router;
    ActorRef fake;

    @Autowired
    public InitActor(PropsFactory propsFactory) {
        this.propsFactory = propsFactory;
    }

    @Override
    public void preStart() throws Exception {
        System.out.println("pre start");
        router = getContext().actorOf(new RandomPool(1000).props(propsFactory.props(ReceiverActor.class)), "router");
        System.out.println("pre start end");


//        fake = getContext().actorOf(propsFactory.props(Fake.class), "fake");
    }

    @Override
    public Receive createReceive() {
        return ReceiveBuilder
                .create()
                .match(SomeMessage.class, someMessage -> {
                    System.out.println(router.path());
                    fake.tell(new SomeMessage(), getSelf());
//                    router.tell(new SomeMessage(), getSelf());
                })
                .build();
    }
}
