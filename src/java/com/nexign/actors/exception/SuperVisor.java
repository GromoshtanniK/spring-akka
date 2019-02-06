package com.nexign.actors.exception;

import akka.actor.AbstractActor;
import akka.actor.ActorLogging;
import akka.actor.ActorRef;
import akka.actor.SupervisorStrategy;
import akka.event.LoggingAdapter;
import akka.japi.pf.ReceiveBuilder;
import com.nexign.configuration.akka.Actor;
import com.nexign.configuration.akka.PropsFactory;
import com.nexign.messages.TickSignal;
import org.springframework.beans.factory.annotation.Autowired;

@Actor
public class SuperVisor extends AbstractActor {

    private final PropsFactory propsFactory;
    private ActorRef subordinate;

    @Autowired
    public SuperVisor(PropsFactory propsFactory) {
        this.propsFactory = propsFactory;
    }

    @Override
    public void preStart() throws Exception {
        subordinate = getContext().actorOf(propsFactory.props(Subordinate.class), "subordinate");
    }

    @Override
    public AbstractActor.Receive createReceive() {
        return ReceiveBuilder
                .create()
                .match(TickSignal.class, tickSignal -> {
                    subordinate.tell(new TickSignal(), getSender());
                    subordinate.tell(new TickSignal(), getSender());
                })
                .build();
    }
}
