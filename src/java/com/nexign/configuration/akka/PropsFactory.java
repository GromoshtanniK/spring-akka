package com.nexign.configuration.akka;

import akka.actor.Actor;
import akka.actor.Extension;
import akka.actor.Props;
import com.nexign.configuration.akka.SpringActorProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class PropsFactory {

    private final ApplicationContext applicationContext;

    @Autowired
    public PropsFactory(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public Props props(Class<? extends Actor> clazz) {
        String[] beanNamesForType = applicationContext.getBeanNamesForType(clazz);

        if (beanNamesForType.length > 1) {
            throw new RuntimeException();
        }

        return Props.create(SpringActorProducer.class, applicationContext, beanNamesForType[0]);
    }
}
