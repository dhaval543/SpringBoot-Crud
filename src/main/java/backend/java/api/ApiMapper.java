package backend.java.api;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import backend.java.entity.Person;
import backend.java.payload.PersonGetResponsePayload;
import backend.java.payload.PersonPatchRequestPayload;
import backend.java.payload.PersonPostRequestPayload;

@Mapper(componentModel = "spring")
public interface ApiMapper {

	PersonGetResponsePayload toPersonGetResponsePayload(Person p);

	@Mapping(target = "id", ignore = true)
	Person toPerson(PersonPostRequestPayload p);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "dateOfBirth", ignore = true)
	Person toPerson(PersonPatchRequestPayload p);

}
