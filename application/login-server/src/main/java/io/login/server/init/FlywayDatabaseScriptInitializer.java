package io.login.server.init;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class FlywayDatabaseScriptInitializer {

    @Value("${flyway.script.location}")
    private String flywayLocation;

    @Autowired
    private DataSource dataSource;

    public void afterPropertiesSet()
    {
        Flyway flyway = Flyway.configure().locations(flywayLocation)
                .dataSource(dataSource).load();
        flyway.repair();
        flyway.migrate();
    }

}
