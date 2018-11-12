package be.ipieter.chat.user;

import be.ipieter.chat.channel.ChannelMember;
import be.ipieter.chat.client.Client;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author Pieter
 * @version 1.0
 */
@Entity
@Data
public class User
{
    @Id
    @JsonIgnore
    @GeneratedValue( strategy = GenerationType.AUTO )
    private Long id;

    @Size( min = 2 )
    private String displayName;

    @NotNull
    private String email;

    private String profileImage;

    private String status;

    @ElementCollection( targetClass = UserRole.class, fetch = FetchType.EAGER )
    @Enumerated( EnumType.STRING )
    @JsonIgnore
    private Set <UserRole> userRoles;

    @JsonIgnore
    private String password;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List <Client> clientList;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<ChannelMember> channelMembers;

    public User()
    {
    }

    public User( String displayName, String email, Set <UserRole> userRoles, String password )
    {
        this.displayName = displayName;
        this.email = email;
        this.userRoles = userRoles;
        this.password = password;
        this.clientList = new ArrayList<>();
    }

    @Override
    public boolean equals( Object o )
    {
        if ( this == o ) return true;
        if ( o == null || getClass() != o.getClass() ) return false;
        if ( !super.equals( o ) ) return false;

        User user = ( User ) o;

        return id != null ? id.equals( user.id ) : user.id == null;
    }

    @Override
    public int hashCode()
    {
        int result = super.hashCode();
        result = 31 * result + ( id != null ? id.hashCode() : 0 );
        return result;
    }

    @Override
    public String toString()
    {
        return "User{" +
                "id=" + id +
                ", displayName='" + displayName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
