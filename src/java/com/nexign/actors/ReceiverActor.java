package com.nexign.actors;

import akka.actor.AbstractActor;
import akka.japi.pf.ReceiveBuilder;
import com.nexign.configuration.akka.Actor;
import com.nexign.messages.SomeMessage;

@Actor
public class ReceiverActor extends AbstractActor {


    @Override
    public Receive createReceive() {
        return ReceiveBuilder
                .create().match(SomeMessage.class, (someMessage -> {
                    System.out.println("got message");
                    System.out.println(getSelf().path());
                    getContext().getParent().tell(new SomeMessage(), getContext().getParent());
                }))
                .build();
    }
}
