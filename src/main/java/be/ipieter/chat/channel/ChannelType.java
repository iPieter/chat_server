package be.ipieter.chat.channel;

/**
 * The ChannelType tells us about what kind of channel members are in, it
 * can be either a direct (person to person) conversation, or a private
 * channel which requires invitations, or a public channel.
 *
 * @author Pieter
 * @version 1.0
 */
public enum ChannelType
{
    DIRECT,
    PRIVATE,
    PUBLIC,
    SELF
}
