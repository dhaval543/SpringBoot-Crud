package backend.api;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import backend.java.api.ApiMapper;
import backend.java.entity.Person;
import backend.java.payload.PersonGetResponsePayload;
import backend.java.payload.PersonPatchRequestPayload;
import backend.java.payload.PersonPostRequestPayload;

public class ApiMapperTest {

	final ApiMapper mapper = Mappers.getMapper(ApiMapper.class);

	static Person person;
	static PersonGetResponsePayload personGetResponsePayload;
	static PersonPostRequestPayload personPostRequestPayload;

	@BeforeAll
	static void beforeAll() {
		String id = "1";
		String string = "string";
		LocalDate localDate = LocalDate.now();

		person = Person.builder().id(id).firstName(string).lastName(string).dateOfBirth(localDate).build();

		personGetResponsePayload = new PersonGetResponsePayload();
		personGetResponsePayload.setId(id);
		personGetResponsePayload.setFirstName(string);
		personGetResponsePayload.setLastName(string);
		personGetResponsePayload.setDateOfBirth(localDate);

		personPostRequestPayload = new PersonPostRequestPayload();
		personPostRequestPayload.setFirstName(string);
		personPostRequestPayload.setLastName(string);
		personPostRequestPayload.setDateOfBirth(localDate);
	}

	@Test
	public void fromPersonToPersonGetResponsePayload() {
		assertEquals(personGetResponsePayload, mapper.toPersonGetResponsePayload(person));
	}

	@Test
	public void fromPersonPostRequestPayloadToPerson() {
		assertEquals(person.toBuilder().id(null).build(), mapper.toPerson(personPostRequestPayload));
	}

	@Test
	public void fromPersonPatchRequestPayloadToPerson() {
		String firstName = "firstName";
		String lastName = "lastName";

		PersonPatchRequestPayload payload = new PersonPatchRequestPayload();
		payload.setFirstName(firstName);
		payload.setLastName(lastName);

		Person expected = Person.builder().firstName(firstName).lastName(lastName).build();
		Person actual = mapper.toPerson(payload);

		assertEquals(expected, actual);
	}

}
