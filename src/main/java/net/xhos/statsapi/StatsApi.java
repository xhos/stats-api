package net.xhos.statsapi;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import com.mojang.brigadier.CommandDispatcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class StatsApi implements ModInitializer {
	public static final String MOD_ID = "statsapi";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	private static MinecraftServer server;
	private static HttpServer httpServer;

	@Override
	public void onInitialize() {
		LOGGER.info("Initializing StatsApi");

		ServerLifecycleEvents.SERVER_STARTED.register(server -> {
			LOGGER.info("Server started");
			StatsApi.server = server;

			httpServer = new HttpServer();
			httpServer.start();
		});

		ServerLifecycleEvents.SERVER_STOPPING.register(server -> {
			if (httpServer != null) {
				httpServer.stop();
			}
		});
	}

	public static int executeCommand(String command) {
		LOGGER.info("Executing command: " + command);
		CommandDispatcher<ServerCommandSource> dispatcher = server.getCommandManager().getDispatcher();
		try {
			int result = dispatcher.execute("stats query " + command, server.getCommandSource());
			LOGGER.info(""+ result);
			return result;
		} catch (Exception e) {
			LOGGER.error("Failed to execute command", e);
		}
        return 0;
    }
}