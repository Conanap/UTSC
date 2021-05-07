import java.io.InputStream;
import org.junit.Test;
import driver.Input;
import static org.junit.Assert.assertEquals;

public class InputTest {
	private Input input = new Input();

	@Test
	public void testInput() {
		System.setIn(new InputStream("Hello"));
		String out = input.getNextInput(System.in);
		assertEquals("Hello", out);
	}
}