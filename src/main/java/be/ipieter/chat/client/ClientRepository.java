package be.ipieter.chat.client;

import be.ipieter.chat.user.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Pieter
 * @version 1.0
 */
@Repository
interface ClientRepository extends PagingAndSortingRepository<Client, Long>
{
    Client findClientByIdentifierKey(String identifierKey);
}
