package backend.java.api;

import backend.java.entity.Employee;
import backend.java.entity.Person;
import backend.java.payload.PersonGetResponsePayload;
import backend.java.payload.PersonPatchRequestPayload;
import backend.java.payload.PersonPostRequestPayload;
import backend.java.usecase.CreatePersonUseCase;
import backend.java.usecase.DeletePersonUseCase;
import backend.java.usecase.ReadPersonUseCase;
import backend.java.usecase.UpdatePersonUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PersonController {

	private final CreatePersonUseCase createUseCase;
	private final ReadPersonUseCase readUseCase;
	private final UpdatePersonUseCase updateUseCase;
	private final DeletePersonUseCase deleteUseCase;
	private final ApiMapper mapper;

	@GetMapping("/persons")
	public List<Employee> getAll() {
			return List.of(new Employee("1","John Doe","test"), new Employee("2","Jane Doe","opopo"));
		}
	
	//public List<PersonGetResponsePayload> getAll() {
		// Return some dummy data for testing
		//return readUseCase.readAll().stream().map(mapper::toPersonGetResponsePayload).collect(Collectors.toList());
	//}
	
	@GetMapping(value = "/persons/{id}")
	PersonGetResponsePayload getById(@PathVariable(value = "id") String id) {
		return mapper.toPersonGetResponsePayload(readUseCase.readById(id));
	}

	@PostMapping(value = "/persons")
	@CrossOrigin(exposedHeaders = { HttpHeaders.LOCATION })
	ResponseEntity<Void> post(@RequestBody PersonPostRequestPayload payload) {
		Person person = createUseCase.create(mapper.toPerson(payload));
		return ResponseEntity.created(URI.create("/persons/" + person.getId())).build();
	}

	@PatchMapping(value = "/persons/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	void patch(@PathVariable(value = "id") String id, @RequestBody PersonPatchRequestPayload payload) {
		updateUseCase.update(id, mapper.toPerson(payload));
	}

	@DeleteMapping(value = "/persons/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	void delete(@PathVariable(value = "id") String id) {
		deleteUseCase.deleteById(id);
	}

}
