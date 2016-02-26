import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CoderEntry {

	private String firstName = "";
	private String lastName = "";
	private float experience = -1;
	private List<String> languages = new ArrayList<String>();
	private List<String> availability = new ArrayList<String>();
	
	public CoderEntry(){
	}
	
	public CoderEntry(String firstName, String lastName, float experience, List<String> languages, List<String> availability) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.experience = experience;
		this.languages = languages;
		this.availability = availability;
	}
	
	public static List<String> getAttributeNames(){
		List<String> list = new ArrayList<String>();
		for (Field f : CoderEntry.class.getDeclaredFields()) {
			list.add(f.getName());
		}
		return list;
	}
	
	public String toString(){
		String str = "Name: " + getFirstName() + " " + getLastName() + "<br>Languages: ";
		
		for (String string : getLanguages()) {
			str += " " + Character.toUpperCase(string.charAt(0)) + string.substring(1);
		}
		str += "<br>Availability: ";
		
		for (String string : getAvailability()) {
			str += " " + Character.toUpperCase(string.charAt(0)) + string.substring(1);
		}
		str += "<br>Experience: " + getExperience() + " years";
		
		return str;
	}
	
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public float getExperience() {
		return experience;
	}
	public void setExperience(float experience) {
		this.experience = experience;
	}
	public List<String> getLanguages() {
		return languages;
	}
	public void setLanguages(List<String> languages) {
		this.languages = languages;
	}
	public List<String> getAvailability() {
		return availability;
	}
	public void setAvailability(List<String> availability) {
		this.availability = availability;
	}
	
	
}
