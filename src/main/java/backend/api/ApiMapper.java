package backend.api;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import backend.api.payload.PersonGetResponsePayload;
import backend.api.payload.PersonPatchRequestPayload;
import backend.api.payload.PersonPostRequestPayload;
import backend.domain.entity.Person;

@Mapper(componentModel = "spring")
interface ApiMapper {

	PersonGetResponsePayload toPersonGetResponsePayload(Person p);

	@Mapping(target = "id", ignore = true)
	Person toPerson(PersonPostRequestPayload p);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "dateOfBirth", ignore = true)
	Person toPerson(PersonPatchRequestPayload p);

}
