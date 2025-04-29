package backend.java.usecase;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import backend.java.PersonData;
import backend.java.entity.Person;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CreatePersonUseCase {

	@Autowired
	private PersonData data;

	public Person create(Person v) {
		if (Objects.isNull(v)) {
			throw new IllegalArgumentException("person to create cannot be null");
		}
		return data.insert(v);
	}

}
