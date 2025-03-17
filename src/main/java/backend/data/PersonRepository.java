package backend.data;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import backend.domain.EntityNotFoundException;
import backend.domain.PersonData;
import backend.domain.entity.Person;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
class PersonRepository implements PersonData {

	private static final String ENTITY_NOT_FOUND_UNDER_ID_MESSAGE = "entity person not found under id = %s";

	@Autowired
	private  PersonMongoRepository mongoRepository;
	@Autowired
	private  DataMapper mapper;

	@Override
	public List<Person> findAll() {
		return mongoRepository.findAll().stream().map(mapper::toPerson).collect(Collectors.toList());
	}

	@Override
	public Person findById(String id) {
		return mongoRepository.findById(id).map(mapper::toPerson)
				.orElseThrow(() -> new EntityNotFoundException(String.format(ENTITY_NOT_FOUND_UNDER_ID_MESSAGE, id)));
	}

	@Override
	public Person insert(Person p) {
		return mapper.toPerson(mongoRepository.save(mapper.toPersonMongo(p)));
	}

	@Override
	public Person update(String id, Person p) {
		PersonMongo toUpdate = mongoRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException(String.format(ENTITY_NOT_FOUND_UNDER_ID_MESSAGE, id)));
		toUpdate.setFirstName(p.getFirstName());
		toUpdate.setLastName(p.getLastName());
		return mapper.toPerson(mongoRepository.save(toUpdate));
	}

	@Override
	public void deleteById(String id) {
		mongoRepository.deleteById(id);
	}

}
