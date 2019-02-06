package com.nexign.configuration;

import akka.actor.*;
import akka.event.Logging;
import com.nexign.actors.DeadLetterActor;
import com.nexign.configuration.akka.PropsFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AkkaConfiguration {

    private final PropsFactory factory;

    @Autowired
    public AkkaConfiguration(PropsFactory factory) {
        this.factory = factory;
    }

    @Bean
    public ActorSystem actorSystem() {
        ActorSystem system = ActorSystem.create("GUS_AGGREGATES_ACTOR_SYSTEM");
        ActorRef actorRef = system.actorOf(factory.props(DeadLetterActor.class));
        system.eventStream().subscribe(actorRef, DeadLetter.class);
        system.eventStream().subscribe(actorRef, UnhandledMessage.class);
        system.eventStream().setLogLevel(Logging.DebugLevel());
        return system;
    }
}
