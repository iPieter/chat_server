package be.ipieter.chat.channel;

import be.ipieter.chat.user.User;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Pieter
 * @version 1.0
 */
@Repository
public interface ChannelMemberRepository extends PagingAndSortingRepository <ChannelMember, Long>
{
    List<ChannelMember> findAllByUser( User user );
}
