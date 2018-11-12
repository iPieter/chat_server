package be.ipieter.chat.channel;

import be.ipieter.chat.client.Client;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Pieter
 * @version 1.0
 */
@Repository
public interface ChannelRepository extends PagingAndSortingRepository <Channel, Long>
{
    Channel findChannelByKey( String key );
}
