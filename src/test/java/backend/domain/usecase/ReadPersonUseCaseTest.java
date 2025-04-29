package backend.domain.usecase;

import backend.java.EntityNotFoundException;
import backend.java.PersonData;
import backend.java.entity.Person;
import backend.java.usecase.ReadPersonUseCase;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReadPersonUseCaseTest {

	@Mock
	private PersonData data;

	@InjectMocks
	private ReadPersonUseCase useCase;

	@Test
	public void readByIdThrowsExceptionWhenIdIsNull() {
		IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> useCase.readById(null));
		assertEquals("person id cannot be null", exception.getMessage());
	}

	@Test
	public void readByIdThrowsExceptionWhenNotFound() {
		when(data.findById(anyString())).thenThrow(EntityNotFoundException.class);

		Assertions.assertThrows(EntityNotFoundException.class, () -> useCase.readById("nonExistentId"));

		verify(data).findById(anyString());
		verifyNoMoreInteractions(data);
	}

	@Test
	public void readById() {
		String id = "someId";
		String string = "string";

		Person expected = Person.builder().id(id).firstName(string).lastName(string).dateOfBirth(LocalDate.now())
				.build();

		when(data.findById(anyString())).thenReturn(expected);

		Person actual = useCase.readById(id);

		assertNotNull(actual);
		assertEquals(expected, actual);

		verify(data).findById(anyString());
		verifyNoMoreInteractions(data);
	}

	@Test
	public void readAll() {
		when(data.findAll()).thenReturn(Collections.emptyList());

		List<Person> list = useCase.readAll();

		assertNotNull(list);
		assertTrue(list.isEmpty());

		verify(data).findAll();
		verifyNoMoreInteractions(data);
	}

}
