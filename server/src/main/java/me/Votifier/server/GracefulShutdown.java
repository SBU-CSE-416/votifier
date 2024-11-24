package me.Votifier.server;

import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Component
public class GracefulShutdown {

    private static final Logger logger = Logger.getLogger(GracefulShutdown.class.getName());

    @EventListener
    public void onApplicationEvent(ContextClosedEvent event) {
        logger.info("Server is shutting down...");
    }
}
