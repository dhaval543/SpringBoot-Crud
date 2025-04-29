package backend.java.data;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface PersonMongoRepository extends MongoRepository<PersonMongo, String>, QuerydslPredicateExecutor<PersonMongo> {
}
