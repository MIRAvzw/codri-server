/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mira.adastra3.server;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

/**
 *
 * @author tim
 */
public class Main {
    public static void main(String[] args) {
        AbstractApplicationContext tApplicationContext = new AnnotationConfigApplicationContext(Services.class);
        tApplicationContext.registerShutdownHook();
    }
}
