package logging;

import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CustomStrategyLogger {

	static public void setup() throws IOException {
		Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
		logger.setLevel(Level.ALL);
		CustomStrategyLoggerFormatter customStrategyLoggerFormatter = new CustomStrategyLoggerFormatter();
		ConsoleHandler consoleHandler = new ConsoleHandler();
		consoleHandler.setFormatter(customStrategyLoggerFormatter);
		consoleHandler.setLevel(Level.ALL);
		logger.addHandler(consoleHandler);
//		FileHandler fileTxt = new FileHandler("custom_strategy.log");
//		fileTxt.setFormatter(customStrategyLoggerFormatter);
//		logger.addHandler(fileTxt);
	}
}
