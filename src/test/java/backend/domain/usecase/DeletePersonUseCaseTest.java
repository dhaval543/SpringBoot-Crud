package backend.domain.usecase;

import backend.domain.EntityNotFoundException;
import backend.domain.PersonData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
public class DeletePersonUseCaseTest {

	@Mock
	private PersonData data;

	@InjectMocks
	private DeletePersonUseCase useCase;

	@Test
	public void deleteByIdThrowExceptionWhenIdIsNull() {
		IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> useCase.deleteById(null));
		assertEquals("person id cannot be null", exception.getMessage());
	}

	@Test
	public void deleteByIdThrowExceptionWhenNotFound() {
		String id = "someId";

		doThrow(new EntityNotFoundException("entity person not found under id = " + id)).when(data)
				.deleteById(anyString());

		EntityNotFoundException exception = Assertions.assertThrows(EntityNotFoundException.class, () -> useCase.deleteById(id));
		assertEquals("entity person not found under id = " + id, exception.getMessage());

		verify(data).deleteById(anyString());
		verifyNoMoreInteractions(data);
	}

	@Test
	public void deleteById() {
		doNothing().when(data).deleteById(anyString());

		useCase.deleteById("someId");

		verify(data).deleteById(anyString());
		verifyNoMoreInteractions(data);
	}

}
