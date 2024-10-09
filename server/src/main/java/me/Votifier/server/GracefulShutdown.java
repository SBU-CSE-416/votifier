package me.Votifier.server;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class GracefulShutdown {

    @EventListener
    public void onApplicationEvent(ContextClosedEvent event) {
        System.out.println("\nServer is shutting down...");
    }
}
