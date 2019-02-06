package com.nexign.actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.japi.pf.ReceiveBuilder;
import akka.routing.SmallestMailboxPool;
import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;
import com.nexign.configuration.akka.Actor;
import com.nexign.configuration.akka.PropsFactory;
import com.nexign.messages.TickSignal;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.ThreadLocalRandom;

@Actor
public class SimpleSaveActor extends AbstractActor {

    Logger logger = LoggerFactory.getLogger(SimpleSaveActor.class);

    private final PropsFactory propsFactory;
    private ActorRef cassandraAccessActor;
    private final PreparedStatement preparedStatement;
    private Counter aggregateCounter;

    private ThreadLocalRandom threadLocalRandom = ThreadLocalRandom.current();


    @Autowired
    public SimpleSaveActor(PropsFactory propsFactory, Session session, MeterRegistry meterRegistry, PreparedStatement preparedStatement) {
        this.propsFactory = propsFactory;
        aggregateCounter = meterRegistry.counter("aggregate_counter");
        this.preparedStatement = preparedStatement;
    }

    @Override
    public void preStart() throws Exception {
        cassandraAccessActor = getContext().actorOf(new SmallestMailboxPool(20).props(propsFactory.props(CassandraAccessActor.class)));
        getSelf().tell(new TickSignal(), getSelf());
    }

    @Override
    public Receive createReceive() {
        return ReceiveBuilder
                .create()
                .match(TickSignal.class, tickSignal -> {
                    int i = threadLocalRandom.nextInt(2000000000);
                    BoundStatement boundStatement = preparedStatement.bind("keka" + i, i);
                    cassandraAccessActor.tell(boundStatement, getSelf());
                })
                .match(ResultSet.class, resultSet -> {
                    getSelf().tell(new TickSignal(), getSelf());
                    aggregateCounter.increment();
                })
                .build();
    }
}
