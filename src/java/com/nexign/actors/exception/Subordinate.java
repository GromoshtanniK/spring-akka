package com.nexign.actors.exception;

import akka.actor.AbstractActor;
import akka.actor.SupervisorStrategy;
import akka.japi.pf.ReceiveBuilder;
import com.nexign.configuration.akka.Actor;
import com.nexign.messages.TickSignal;
import scala.Option;

@Actor
public class Subordinate extends AbstractActor {


    public Subordinate() {
        SupervisorStrategy supervisorStrategy = supervisorStrategy();
        System.out.println(supervisorStrategy);
    }

    @Override
    public void preStart() throws Exception {
        System.out.println("postStart");
    }

    @Override
    public void postStop() throws Exception {
        System.out.println("postStop");
    }

    @Override
    public void preRestart(Throwable reason, Option<Object> message) throws Exception {
        System.out.println("preRestart");
    }


    @Override
    public void postRestart(Throwable reason) throws Exception {
        System.out.println("postRestart");
    }


    @Override
    public AbstractActor.Receive createReceive() {
        return ReceiveBuilder
                .create()
                .match(TickSignal.class, tickSignal -> {
                    throw new RuntimeException("KEKA");
                })
                .build();
    }
}
