import java.util.regex.*;


public class EmailValidator {
	
	private Pattern pattern;
	private Matcher match;

	public EmailValidator() {
	  String emailPat = "[\\w-_]+(\\.[\\w-_]+)?@[\\w-_\\.]+\\.[a-z]{2,}";
	  this.pattern = Pattern.compile(emailPat);
	}	
	
	public boolean validateEmail(String email) {
	  this.match = this.pattern.matcher(email);
	  return this.match.matches();
	}

}

