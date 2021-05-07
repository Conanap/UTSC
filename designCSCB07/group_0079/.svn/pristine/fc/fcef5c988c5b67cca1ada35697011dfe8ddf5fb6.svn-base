import java.util.regex.*;


public class UsernameValidator {
	
	private Pattern pattern;
	private Matcher match;

	public UsernameValidator() {
		// TODO
		// create your pattern object with the compile method and your regex
	  String userNamePat = "[a-z0-9\\-_]{3,15}";
	  this.pattern = Pattern.compile(userNamePat);
	}	
	
	public boolean validateUsername(String username) {
		// TODO
		// use the pattern object created in the constructor with the matches method
		// to check for matches
	  this.match = this.pattern.matcher(username);
	  return this.match.matches();
	}
	
}

