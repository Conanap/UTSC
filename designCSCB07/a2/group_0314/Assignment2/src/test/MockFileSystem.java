import filesystem.FileSystem;
public class MockFileSystem extends FileSystem{
	private boolean empty = true;
	//private MockDirectory root = new MockDirectory();
	private MockDirStack ds = new MockDirStack();

	public MockFileSystem () {

	}
/*
	public MockDirectory getDirectory(String path) {
		if(path.equals("."))
			return this.root;
		else
			return this.root.getDirectory(path);
	}
*/
	public void getFile(String path) {
		return;
	}

	public String pop() {
		return ds.pop();
	}

	public void pushd(String path) {
		ds.push(path);
	}
}