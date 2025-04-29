package backend.java.data;

import org.mapstruct.Mapper;

import backend.java.entity.Person;

@Mapper(componentModel = "spring")
public interface DataMapper {

	PersonMongo toPersonMongo(Person p);

	Person toPerson(PersonMongo p);

}
