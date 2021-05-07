import org.junit.Test;
import static org.junit.Assert.assertEquals;

import org.junit.Before;

import static org.junit.Assert.*;
import command.Echo;

public class EchoTest {
	private Echo echo = new Echo();
	private MockOutput out = new MockOutput();

	@Before
	public void setup() {
		echo.initialize(null, null, null);
		out.fflushOut();
	}
	@Test
  public void testValidEcho() {
  		String[] in = {"echo", "\"hello world\""};
  		echo.execute(null, null, out);
		assertEquals("hello world", out.getOutBuffer());
	}

	public void testValidate() {
		String[] in = {"echo", "\"yo\""};
		assertTrue(echo.validate(in));
		in[1] = "yo";
		assertFalse(echo.validate(in));
	}
}
