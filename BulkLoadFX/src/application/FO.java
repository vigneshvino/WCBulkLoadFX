package application;

public class FO {

	@Override
	public String toString() {
		return "FO [name=" + name + ", iteration=" + iteration + ", lifecycle_state=" + lifecycle_state + "]";
	}

	private String name;
	
	private String iteration;
	
	private String lifecycle_state;
	
	public FO() {}

	public FO(String name, String iteration, String lifecycle_state) {
		super();
		this.name = name;
		this.iteration = iteration;
		this.lifecycle_state = lifecycle_state;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIteration() {
		return iteration;
	}

	public void setIteration(String iteration) {
		this.iteration = iteration;
	}

	public String getLifecycle_state() {
		return lifecycle_state;
	}

	public void setLifecycle_state(String lifecycle_state) {
		this.lifecycle_state = lifecycle_state;
	}
	
}
