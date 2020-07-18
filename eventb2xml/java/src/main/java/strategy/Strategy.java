package strategy;

import java.util.List;

public class Strategy {

	private String apiVersion;
	private String kind;
	private Metadata metadata;
	private List<String> expression;
	private List<String> events;
	private Integer steps;
	private Integer timeout;
	private Integer count;

	public String getApiVersion() {
		return apiVersion;
	}

	public void setApiVersion(String apiVersion) {
		this.apiVersion = apiVersion;
	}

	public String getKind() {
		return kind;
	}

	public void setKind(String kind) {
		this.kind = kind;
	}

	public Metadata getMetadata() {
		return metadata;
	}

	public void setMetadata(Metadata metadata) {
		this.metadata = metadata;
	}

	public List<String> getExpression() {
		return expression;
	}

	public void setExpression(List<String> expression) {
		this.expression = expression;
	}

	public List<String> getEvents() {
		return events;
	}

	public void setEvents(List<String> events) {
		this.events = events;
	}

	public Integer getSteps() {
		return steps;
	}

	public void setSteps(Integer steps) {
		this.steps = steps;
	}

	public Integer getTimeout() {
		return timeout;
	}

	public void setTimeout(Integer timeout) {
		this.timeout = timeout;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	@Override
	public String toString() {
		return "Strategy [apiVersion=" + apiVersion + ", kind=" + kind + ", metadata=" + metadata + ", expression="
				+ expression + ", events=" + events + ", steps=" + steps + ", timeout=" + timeout + ", count="
				+ count + "]";
	}

}
