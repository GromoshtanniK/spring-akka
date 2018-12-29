package com.nexign.actors;

import akka.actor.AbstractActor;
import akka.japi.pf.ReceiveBuilder;
import akka.pattern.Patterns;
import com.datastax.driver.core.*;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.nexign.configuration.akka.Actor;
import org.springframework.beans.factory.annotation.Autowired;
import scala.concurrent.Future;
import scala.concurrent.impl.Promise;

@Actor
public class CassandraActor extends AbstractActor {

    private final Session session;

    @Autowired
    public CassandraActor(Session session) {
        this.session = session;
    }

    private <T> Future<T> toScala(ListenableFuture<T> listenableFuture) {
        Promise.DefaultPromise<T> promise = new Promise.DefaultPromise<>();
        Futures.addCallback(listenableFuture, new FutureCallback<T>() {
            @Override
            public void onSuccess(T result) {
                promise.success(result);
            }

            @Override
            public void onFailure(Throwable t) {
                promise.failure(t);
            }
        });

        return promise;
    }


    @Override
    public Receive createReceive() {
        return ReceiveBuilder
                .create()
                .match(Statement.class, statement -> {
                    Future<ResultSet> resultSetFuture = toScala(session.executeAsync(statement));
                    Patterns.pipe(resultSetFuture, getContext().dispatcher()).to(getSender());

                })
                .build();
    }
}
