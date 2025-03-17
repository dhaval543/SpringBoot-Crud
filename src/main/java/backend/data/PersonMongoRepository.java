package backend.data;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

interface PersonMongoRepository extends MongoRepository<PersonMongo, String>, QuerydslPredicateExecutor<PersonMongo> {
}
