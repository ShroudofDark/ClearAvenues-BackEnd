package edu.odu.clearavenues.prototype.user;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

// "Repository" files are where database methods are implemented
// You don't actually need to implement the methods yourself
// Spring Boot will implement them on runtime based on the method name
public interface UserRepository extends CrudRepository<User, Integer> {

    // For example, I don't have to implement the SQL query for this method myself. Spring Boot implements it
    // automatically during runtime.
    // This method will return all users of a specific account Type
    Iterable<User> findByAccountType(String type);


    // However, you can still write a query yourself if you'd like
    @Query(value = "SELECT * FROM users WHERE account_type = :type", nativeQuery = true)
    Iterable<User> nativeSQLFindByAccountType(@Param("type") String type);
}
