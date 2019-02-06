package com.nexign.configuration;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Slf4jReporter;
import io.micrometer.core.instrument.Clock;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.dropwizard.DropwizardConfig;
import io.micrometer.core.instrument.dropwizard.DropwizardMeterRegistry;
import io.micrometer.core.instrument.util.HierarchicalNameMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class MetricsConfiguration {

    @Bean
    public MetricRegistry dropWizardRegistry() {
        return new MetricRegistry();
    }

    @Bean
    public MeterRegistry meterRegistry(MetricRegistry dropWizardRegistry) {
        DropwizardConfig consoleConfig = new DropwizardConfig() {
            @Override
            public String prefix() {
                return null;
            }

            @Override
            public String get(String key) {
                return null;
            }
        };

        return new DropwizardMeterRegistry(consoleConfig, dropWizardRegistry,
                HierarchicalNameMapper.DEFAULT, Clock.SYSTEM) {
            @Override
            protected Double nullGaugeValue() {
                return null;
            }
        };
    }

    @Bean
    public Slf4jReporter reporter(MetricRegistry dropWizardRegistry) {
        Slf4jReporter reporter = Slf4jReporter.forRegistry(dropWizardRegistry)
                .convertRatesTo(TimeUnit.SECONDS)
                .convertDurationsTo(TimeUnit.MILLISECONDS)
                .filter((s, metric) -> s.equals("aggregate_counter"))
                .build();
        reporter.start(1, TimeUnit.SECONDS);
        return reporter;
    }
}