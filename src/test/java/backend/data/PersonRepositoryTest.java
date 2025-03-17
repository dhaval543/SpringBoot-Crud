package backend.data;

import backend.domain.EntityNotFoundException;
import backend.domain.entity.Person;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Testcontainers
public class PersonRepositoryTest {

	@Container
	static final MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:4.2");

	@DynamicPropertySource
	static void setProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
	}

	@Autowired
	PersonRepository repository;

	static Person nikolaTeslaNoId;
	static Person albertEinsteinNoId;
	static Person carlSaganNoId;

	@BeforeAll
	static void beforeAll() {
		nikolaTeslaNoId = Person.builder().firstName("Nikola").lastName("Tesla").dateOfBirth(LocalDate.of(1856, 7, 10))
				.build();
		albertEinsteinNoId = Person.builder().firstName("Albert").lastName("Einstein")
				.dateOfBirth(LocalDate.of(1879, 3, 14)).build();
		carlSaganNoId = Person.builder().firstName("Carl").lastName("Sagan").dateOfBirth(LocalDate.of(1934, 11, 9))
				.build();
	}

	@Test
	public void findAll() {
		Person nt = repository.insert(nikolaTeslaNoId);
		Person ae = repository.insert(albertEinsteinNoId);

		List<Person> findAll = repository.findAll();

		assertNotNull(findAll);
		assertEquals(2, findAll.size());
		assertEquals(Arrays.asList(nt, ae), findAll);

		repository.deleteById(nt.getId());
		repository.deleteById(ae.getId());
	}

	@Test
	public void findById() {
		Person nt = repository.insert(nikolaTeslaNoId);

		Person found = repository.findById(nt.getId());

		assertNotNull(found);
		assertEquals(nt, found);

		repository.deleteById(nt.getId());
	}

	@Test
	public void findByIdNotFound() {
		String nonExistingId = "NonExistingId";
		Exception exception = Assertions.assertThrows(EntityNotFoundException.class, () -> repository.findById(nonExistingId));
		assertEquals("entity person not found under id = " + nonExistingId, exception.getMessage());
	}

	@Test
	public void insert() {
		Person nt = repository.insert(nikolaTeslaNoId);

		assertNotNull(nt);
		assertEquals(nikolaTeslaNoId.toBuilder().id(nt.getId()).build(), nt);

		repository.deleteById(nt.getId());
	}

	@Test
	public void update() {
		Person cs = repository.insert(carlSaganNoId);
		String updatedFirstName = "CARL";
		String updatedLastName = "SAGAN";

		Person toUpdate = cs.toBuilder().id("differentId").firstName(updatedFirstName).lastName(updatedLastName)
				.dateOfBirth(LocalDate.now()).build();

		Person updated = repository.update(cs.getId(), toUpdate);

		assertNotNull(updated);

		assertEquals(cs.getId(), updated.getId());
		assertEquals(cs.getDateOfBirth(), updated.getDateOfBirth());
		assertEquals(updatedFirstName, updated.getFirstName());
		assertEquals(updatedLastName, updated.getLastName());

		repository.deleteById(cs.getId());
	}

	@Test
	public void updateNotFound() {
		String nonExistingId = "NonExistingId";
		Exception exception = Assertions.assertThrows(EntityNotFoundException.class, () -> repository.update(nonExistingId, Person.builder().build()));
		assertEquals("entity person not found under id = " + nonExistingId, exception.getMessage());
	}

	@Test
	public void deleteById() {
		Person cs = repository.insert(carlSaganNoId);

		repository.deleteById(cs.getId());

		Exception exception = Assertions.assertThrows(EntityNotFoundException.class, () -> repository.findById(cs.getId()));
		assertEquals("entity person not found under id = " + cs.getId(), exception.getMessage());
	}

}
