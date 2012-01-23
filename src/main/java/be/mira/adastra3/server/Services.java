/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mira.adastra3.server;

import be.mira.adastra3.server.beans.Controller;
import be.mira.adastra3.server.beans.Network;
import be.mira.adastra3.server.beans.NetworkMonitor;
import be.mira.adastra3.server.beans.Repository;
import be.mira.adastra3.server.beans.RepositoryMonitor;
import be.mira.adastra3.server.beans.factory.LoggerPostProcessor;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.DefaultResourceLoader;

/**
 *
 * @author tim
 */
@Configuration
public class Services {
    //
    // Special beans (processors, configurers, ...)
    //
    
    @Bean
    public static PropertyPlaceholderConfigurer propertyPlaceholderConfigurer() {
        PropertyPlaceholderConfigurer tPropertyPlaceholderConfigurer = new PropertyPlaceholderConfigurer();
        tPropertyPlaceholderConfigurer.setLocation(new DefaultResourceLoader().getResource("classpath:" + "defaults.properties"));
        return tPropertyPlaceholderConfigurer;
    }
    
    @Bean
    public static LoggerPostProcessor loggerPostProcessor() {
        return new LoggerPostProcessor();
    }
    
    
    //
    // Repository beans
    //
    
    @Bean
    public Repository repository() {
        return new Repository();
    }
    
    @Bean
    public RepositoryMonitor repositoryMonitor() {
        return new RepositoryMonitor(repository());
    }
    
    
    //
    // Network beans
    //
    
    @Bean
    public Network network() {
        return new Network();
    }
    
    @Bean
    public NetworkMonitor networkMonitor() {
        return new NetworkMonitor(network());
    }
    
    
    //
    // Controller bean
    //
    
    @Bean
    public Controller controller() {
        return new Controller(network(), repository());
    }
}
