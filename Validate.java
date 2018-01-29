
public class Validate {
	public static boolean isValidName(String s){
		//method to validate username
		String regex="[A-Za-z]+";
				return s.matches(regex)&&s.length()>5&&s.length()<21;
	}

	public static boolean isValidEmail(String s){
		//method to validate email
		String regex="^[A-Z0-9a-z._-]+@[A-Za-z0-9]+.[A-Za-z]+";
				return s.matches(regex);
	}
	
	public static boolean isValidPassword(String s){
		//method to validate password
		return s.length()>5&&s.length()<13;
	}
	
	public static boolean isValidLevel(String s){
		//method to validate level
		String regex="[1-4]";
				return s.matches(regex);
	}
}
