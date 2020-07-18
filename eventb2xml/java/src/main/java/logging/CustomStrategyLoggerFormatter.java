package logging;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class CustomStrategyLoggerFormatter extends Formatter {

	private String calcDate(long millisecs) {
		SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		Date resultdate = new Date(millisecs);
		return date_format.format(resultdate);
	}

	@Override
	public synchronized String format(LogRecord record) {
//		String source;
//		if (record.getSourceClassName() != null) {
//			source = record.getSourceClassName();
//			if (record.getSourceMethodName() != null) {
//				source += " " + record.getSourceMethodName();
//			}
//		} else {
//			source = record.getLoggerName();
//		}
		String message = formatMessage(record);
//		String throwable = "";
//		if (record.getThrown() != null) {
//			StringWriter sw = new StringWriter();
//			PrintWriter pw = new PrintWriter(sw);
//			pw.println();
//			record.getThrown().printStackTrace(pw);
//			pw.close();
//			throwable = sw.toString();
//		}
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(calcDate(record.getMillis())).append(" | ");
//		stringBuilder.append("[").append(source)append("]").append(" ");
//		stringBuilder.append(record.getLevel().getLocalizedName()).append(" ");
		stringBuilder.append(message).append(" ");
		return stringBuilder.append("\n").toString();
	}

}
