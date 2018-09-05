
public class Time implements Runnable {

	private long time;

	public Time(long time) {
		this.time = time;
	}

	public void run() {
		long currentTime = System.currentTimeMillis();
		long expectedTime = currentTime + 1000;
		while (true) {
			currentTime = System.currentTimeMillis();
			if(currentTime >= expectedTime) {
				currentTime = System.currentTimeMillis();
				expectedTime = currentTime + 1000;
				time -= 1000;
			}
		}
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

}
