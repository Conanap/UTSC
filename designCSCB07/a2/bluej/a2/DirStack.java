import java.util.Stack;
import java.util.EmptyStackException;

public class DirStack {
	private Stack<String> stack;

	public DirStack () {
		this.stack = new Stack<String>();
	}

	public String popDir() {
		String path = "";
		try {
			path = this.stack.pop();
		}
		catch (EmptyStackException e) {
		}
		return (path);
	}

	public void pushDir(String path) {
		this.stack.push(path);
	}
}