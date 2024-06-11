package tpe;

public class Tarea {
	private String id;
	private String name;
	private int time;
	private boolean critic;
	int priority;

	public Tarea(String id, String name, int time, boolean critic, int priority){
		this.id = id;
		this.name = name;
		this.time = time;
		this.critic = critic;
		this.priority = priority;
	}
	
	public String getId(){
		return this.id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public boolean isCritic() {
		return critic;
	}

	public void setCritic(boolean critic) {
		this.critic = critic;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}
	
	public String toString(){
		return " " + this.getPriority();
	}
	
	
}