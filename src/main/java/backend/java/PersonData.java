package backend.java;

import java.util.List;

import backend.java.entity.Person;

public interface PersonData {

	List<Person> findAll();

	Person findById(String id);

	Person insert(Person v);

	Person update(String id, Person v);

	void deleteById(String id);

}
