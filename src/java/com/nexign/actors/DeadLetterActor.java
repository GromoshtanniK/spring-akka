package com.nexign.actors;

import akka.actor.AbstractActor;
import akka.actor.DeadLetter;
import akka.actor.UnhandledMessage;
import com.nexign.configuration.akka.Actor;

@Actor
public class DeadLetterActor extends AbstractActor {
    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(DeadLetter.class, msg -> {
                    System.out.println(msg);
                })
                .match(UnhandledMessage.class, msg -> {
                    System.out.println(msg);
                })
                .build();
    }
}

