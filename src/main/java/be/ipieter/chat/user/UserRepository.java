package be.ipieter.chat.user;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Pieter
 * @version 1.0
 */
@Repository
interface UserRepository extends PagingAndSortingRepository<User, Long>
{
    User findByEmailIgnoreCase(String email);
}
