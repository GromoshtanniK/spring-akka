package com.nexign.actors;

import akka.actor.AbstractActor;
import akka.japi.pf.ReceiveBuilder;
import com.nexign.configuration.akka.Actor;

@Actor
public class FileParser extends AbstractActor {
    @Override
    public Receive createReceive() {
        return ReceiveBuilder.create().build();
    }
}
