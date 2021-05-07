
public class TestEmailValidator {


	public static void main(String[] args) {
		System.out.println("Valid emails:\n");

		EmailValidator emailValidator = new EmailValidator();
		
		String[] emails = new String [] {"john23@gmail.com", 
										 "john_23@mail.utoronto.ca", 
										 "John.doe@long-domaim.name.com", 
										 "Pokemon-GO-is-the-best@AOL.com"};
		for (String email: emails){
			if (emailValidator.validateEmail(email)){
				System.out.println(email + " is a valid username.");
			} else {
				System.out.println(email + " is an invalid username.");
			}
		}
		System.out.println("\nInvalid emails:\n");

		String[] bademails = new String [] {"john.@gmail.com", 
										 "john_23@mail.utoronto.c", 
										 "John.doe@name.ca123", 
										 "pokemon-is-the-best!@aol.com"};		
		for (String email1: bademails){
			if (emailValidator.validateEmail(email1)){
				System.out.println(email1 + " is a valid username.");
			} else {
				System.out.println(email1 + " is an invalid username.");
			}
		}
		
	}

}
