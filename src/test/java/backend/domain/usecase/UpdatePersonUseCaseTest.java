package backend.domain.usecase;

import backend.java.PersonData;
import backend.java.entity.Person;
import backend.java.usecase.UpdatePersonUseCase;

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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UpdatePersonUseCaseTest {

	@Mock
	private PersonData data;

	@InjectMocks
	private UpdatePersonUseCase useCase;

	@Test
	public void throwExceptionWhenIdIsNull() {
		IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> useCase.update(null, Person.builder().build()));
		assertEquals("person id cannot be null", exception.getMessage());
	}

	@Test
	public void throwExceptionWhenPersonIsNull() {
		IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> useCase.update("randomId", null));
		assertEquals("person to update cannot be null", exception.getMessage());
	}

	@Test
	public void update() {
		String id = "someId";

		String updatedFirstName = "updatedFirstName";
		String updatedLastName = "updatedLastName";

		Person expected = Person.builder().id(id).firstName(updatedFirstName).lastName(updatedLastName)
				.dateOfBirth(LocalDate.now()).build();

		when(data.update(anyString(), any(Person.class))).thenReturn(expected);

		Person toUpdate = Person.builder().id("someOtherId").firstName(updatedFirstName).lastName(updatedLastName)
				.dateOfBirth(LocalDate.of(2021, 1, 1)).build();

		Person actual = useCase.update(id, toUpdate);

		assertNotNull(actual);
		assertEquals(expected, actual);

		verify(data).update(anyString(), any(Person.class));
		verifyNoMoreInteractions(data);
	}

}
