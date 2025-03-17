package backend.api;

import backend.api.payload.PersonGetResponsePayload;
import backend.api.payload.PersonPatchRequestPayload;
import backend.api.payload.PersonPostRequestPayload;
import backend.domain.entity.Person;
import backend.domain.usecase.CreatePersonUseCase;
import backend.domain.usecase.DeletePersonUseCase;
import backend.domain.usecase.ReadPersonUseCase;
import backend.domain.usecase.UpdatePersonUseCase;
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
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/persons")
@RequiredArgsConstructor
class PersonController {

    private final CreatePersonUseCase createUseCase;
    private final ReadPersonUseCase readUseCase;
    private final UpdatePersonUseCase updateUseCase;
    private final DeletePersonUseCase deleteUseCase;
    private final ApiMapper mapper;

    @GetMapping
    List<PersonGetResponsePayload> getAll() {
        return readUseCase.readAll().stream().map(mapper::toPersonGetResponsePayload)
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/{id}")
    PersonGetResponsePayload getById(@PathVariable(value = "id") String id) {
        return mapper.toPersonGetResponsePayload(readUseCase.readById(id));
    }

    @PostMapping
    @CrossOrigin(exposedHeaders = {HttpHeaders.LOCATION})
    ResponseEntity<Void> post(@RequestBody PersonPostRequestPayload payload) {
        Person person = createUseCase.create(mapper.toPerson(payload));
        return ResponseEntity.created(URI.create("/persons/" + person.getId())).build();
    }

    @PatchMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void patch(@PathVariable(value = "id") String id, @RequestBody PersonPatchRequestPayload payload) {
        updateUseCase.update(id, mapper.toPerson(payload));
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void delete(@PathVariable(value = "id") String id) {
        deleteUseCase.deleteById(id);
    }

}
