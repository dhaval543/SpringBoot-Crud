package backend.domain.usecase;

import backend.java.PersonData;
import backend.java.entity.Person;
import backend.java.usecase.CreatePersonUseCase;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CreatePersonUseCaseTest {

	@Mock
	private PersonData data;

	@InjectMocks
	private CreatePersonUseCase useCase;

	@Test
	public void throwExceptionWhenNull() {
		IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> useCase.create(null));
		assertEquals("person to create cannot be null", exception.getMessage());
	}

	@Test
	public void create() {
		String string = "string";
		LocalDate localDate = LocalDate.now();

		Person expected = Person.builder().id("someId").firstName(string).lastName(string).dateOfBirth(localDate)
				.build();

		when(data.insert(any(Person.class))).thenReturn(expected);

		Person personToCreate = Person.builder().firstName(string).lastName(string).dateOfBirth(localDate).build();

		Person actual = useCase.create(personToCreate);

		assertNotNull(actual);
		assertEquals(expected, actual);

		verify(data).insert(any(Person.class));
		verifyNoMoreInteractions(data);
	}

}
