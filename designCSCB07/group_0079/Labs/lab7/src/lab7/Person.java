package lab7;

public class Person {

	//there should be 6 fields
	private final String lastName; 	// required
	private final String firstName;
	private String address;
	private final String dateOfBirth;
	private String phoneNumber;
	private String email; 	// optional
	
	public static class Builder {

		// required parameters (3)
		private final String lastName;
		private final String firstName;
	    private final String dateOfBirth;
		//TODO

		// optional parameters (3)-initialized to default values
		// (of course these should be more reasonable default values)
		// why are these not final?
		private String phoneNumber = "Not provided";
		private String address = "Not provided";
		private String email = "Not provided";
		//TODO

		// Builder constructor with required fields (3)
		public Builder(String firstName, String lastName, String dateOfBirth) {
			//TODO
		  this.lastName = lastName;
		  this.firstName = firstName;
		  this.dateOfBirth = dateOfBirth;
		}

		//methods below are to change the default values of the optional parameters
		public Builder address(String address) {
			//TODO
		  this.address = address;
		  return this;
		}

		public Builder phoneNumber(String phoneNumber) {
			//TODO
		    this.phoneNumber = phoneNumber;
		    return this;
		}

		public Builder email(String email) {
			//TODO
		  this.email = email;
		  return this;
		}

		public Person build() {
			//TODO
		  return new Person(this);
		}
	}

    public Person(Builder b) { // private constructor of the outer Person class
      this.lastName = b.lastName;
      this.firstName = b.firstName;
      this.email = b.email;
      this.address = b.address;
      this.dateOfBirth = b.dateOfBirth;
      this.phoneNumber = b.phoneNumber;
    }
	public String toString() {
		String fname = "First name: " + this.firstName;
		String lname = "Last name: " + this.lastName;
		String address = "Address: " + this.address;
		String dob = "Date of birth: " + this.dateOfBirth;
		String number = "Number: " + this.phoneNumber;
		String email = "Email: " + this.email;
		return fname + "\n" + lname + "\n" + dob + "\n" + address + "\n" + number + "\n" + email + "\n";
	}

}
