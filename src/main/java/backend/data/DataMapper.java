package backend.data;

import org.mapstruct.Mapper;

import backend.domain.entity.Person;

@Mapper(componentModel = "spring")
interface DataMapper {

	PersonMongo toPersonMongo(Person p);

	Person toPerson(PersonMongo p);

}
