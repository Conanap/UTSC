public class MockDirStack {
	String path = null;
	public String pop() {
		return this.path;
	}
	public void push(String path) {
		this.path = path;
	}
}