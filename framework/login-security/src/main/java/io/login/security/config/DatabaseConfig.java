package io.login.security.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Getter
@Setter
@Configuration
public class DatabaseConfig {

    @Value("${db.datasource.url}")
    private String url;
    @Value("${db.datasource.username}")
    private String username;
    @Value("${db.datasource.password}")
    private String password;
    @Value("${db.datasource.driver-classname}")
    private String driverClassName;

    @Bean(name = "dataSource")
    public DataSource mysqlDataSource()
    {
        DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
        driverManagerDataSource.setDriverClassName(driverClassName);
        driverManagerDataSource.setUrl(url);
        driverManagerDataSource.setUsername(username);
        driverManagerDataSource.setPassword(password);
        return driverManagerDataSource;
    }
}
