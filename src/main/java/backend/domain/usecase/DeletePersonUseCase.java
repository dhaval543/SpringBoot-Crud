package backend.domain.usecase;

import java.util.Objects;

import org.springframework.stereotype.Component;

import backend.domain.PersonData;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DeletePersonUseCase {

	private PersonData data;

	public void deleteById(String id) {
		if (Objects.isNull(id)) {
			throw new IllegalArgumentException("person id cannot be null");
		}
		data.deleteById(id);
	}

}
