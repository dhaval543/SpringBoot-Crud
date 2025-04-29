package backend.java.entity;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Builder(toBuilder = true)
public class Person {
	

	private String id;
	private String firstName;
	private String lastName;
	private LocalDate dateOfBirth;
		
	
	
	

}
