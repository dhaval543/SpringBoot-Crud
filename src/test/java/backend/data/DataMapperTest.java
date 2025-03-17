package backend.data;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import backend.domain.entity.Person;

public class DataMapperTest {

	final DataMapper mapper = Mappers.getMapper(DataMapper.class);

	static Person person;
	static PersonMongo personMongo;

	@BeforeAll
	static void beforeAll() {
		String id = "id";
		String string = "string";
		LocalDate localDate = LocalDate.now();

		person = Person.builder().id(id).firstName(string).lastName(string).dateOfBirth(localDate).build();
		personMongo = new PersonMongo(id, string, string, localDate);
	}

	@Test
	public void fromPersonToPersonMongo() {
		assertEquals(personMongo, mapper.toPersonMongo(person));
	}

	@Test
	public void fromPersonMongoToPerson() {
		assertEquals(person, mapper.toPerson(personMongo));
	}

}
