package net.xhos.statsapi;

import io.javalin.Javalin;
import java.util.logging.Logger;

public class HttpServer {
    private final Javalin app;
    private static final Logger LOGGER = Logger.getLogger(HttpServer.class.getName());

    public HttpServer() {
        LOGGER.info("Initializing HttpServer");
        app = Javalin.create();
        app.get("/api/executeCommand", ctx -> {
            String command = ctx.queryParam("command");
            LOGGER.info("Received command: " + command);
            int result = StatsApi.executeCommand(command);
            ctx.result("" + result);
            LOGGER.info("Command executed");
        });
    }

    public void start() {
        LOGGER.info("Starting HttpServer on port 8081");
        app.start(8081);
    }

    public void stop() {
        LOGGER.info("Stopping HttpServer");
        app.stop();
    }
}