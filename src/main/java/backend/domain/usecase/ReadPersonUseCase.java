package backend.domain.usecase;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import backend.domain.PersonData;
import backend.domain.entity.Person;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ReadPersonUseCase {
	
	@Autowired
	private PersonData data;

	public Person readById(String id) {
		if (Objects.isNull(id)) {
			throw new IllegalArgumentException("person id cannot be null");
		}
		return data.findById(id);
	}

	public List<Person> readAll() {
		return data.findAll();
	}

}
