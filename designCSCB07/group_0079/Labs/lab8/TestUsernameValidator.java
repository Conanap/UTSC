
public class TestUsernameValidator {

	public static void main(String[] args) {
		
		System.out.println("Valid usernames:\n");

		UsernameValidator usernameValidator = new UsernameValidator();
		
		String[] validUsernames = new String [] {"john23", 
												"john_23", 
												"jd1996", 
												"johndoe1996"};
		for (String username: validUsernames){
			if (usernameValidator.validateUsername(username)){
				System.out.println(username + " is a valid username.");
			} else {
				System.out.println(username + " is an invalid username.");
			}
		}
		
		System.out.println("\nInvalid usernames:\n");

		String[] invalidUsernames = new String [] {"John23", 
												  "john.23", 
												  "jd", 
												  "john_doe1996-123"};
		for (String username1: invalidUsernames){
			if (usernameValidator.validateUsername(username1)){
				System.out.println(username1 + " is a valid username.");
			} else {
				System.out.println(username1 + " is an invalid username.");
			}
		}

	}
		
}


