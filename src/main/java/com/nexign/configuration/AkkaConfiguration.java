package com.nexign.configuration;

import akka.actor.ActorSystem;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AkkaConfiguration {

    @Bean
    public ActorSystem actorSystem() {
        ActorSystem system = ActorSystem.create("GUS_AGGREGATES_ACTOR_SYSTEM");
        return system;
    }
}
