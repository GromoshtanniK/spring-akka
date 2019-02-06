package com.nexign.configuration;

import com.datastax.driver.core.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CassandraConfig {


    @Bean
    public Cluster cluster() {

        PoolingOptions poolingOptions = new PoolingOptions();
        poolingOptions.setConnectionsPerHost(HostDistance.LOCAL, 8, 8);
        poolingOptions.setMaxRequestsPerConnection(HostDistance.LOCAL, 32000);
        poolingOptions.setMaxQueueSize(2000000000);


        return Cluster.builder()

                .addContactPoints("localhost")
                .withPort(9042)
                .withClusterName("GUS")
                .withPoolingOptions(poolingOptions)
                .build();
    }

    @Bean
        public Session cassandraSession(Cluster cluster) {
        return cluster.connect("gus");
    }

    @Bean
    public PreparedStatement preparedStatement(Session session) {
        return session.prepare("INSERT INTO race_winners (race_name, race_position) VALUES (?, ?)");
    }
}
