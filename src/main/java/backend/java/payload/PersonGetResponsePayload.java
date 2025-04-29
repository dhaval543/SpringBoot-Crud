package backend.java.payload;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class PersonGetResponsePayload {

	private String id;
	private String firstName;
	private String lastName;

	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate dateOfBirth;

}
