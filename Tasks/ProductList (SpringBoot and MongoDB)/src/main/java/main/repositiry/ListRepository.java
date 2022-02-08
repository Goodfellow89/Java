package main.repositiry;

import main.model.ProductList;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ListRepository extends MongoRepository<ProductList, Long> {
}
