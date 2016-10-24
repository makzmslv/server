package backend.server.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import backend.business.enums.UserAuthorities;
import backend.db.dao.CustomerAccountDetailsDAO;
import backend.db.entity.CustomerAccountDetailsEntity;

/**
 * This service is used for authenticating users using Spring-security. It verifies whether a user is registered in the database and assigns appropriate authority to the user i.e either ADMIN OR USER.
 *
 */
public class LoginAuthenticationImpl implements UserDetailsService
{
    @Autowired
    private CustomerAccountDetailsDAO customerAccountDetailsDAO;

    /**
     * Checks whether specified username/password is present in the database. If yes, allows access access to the user.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {

        CustomerAccountDetailsEntity customerAccountDetailsEntity = customerAccountDetailsDAO.findByUserNameAndEnabled(username, true);

        org.springframework.security.core.userdetails.User userDetail = new org.springframework.security.core.userdetails.User(customerAccountDetailsEntity.getUserName(), customerAccountDetailsEntity.getPassword(), true, true, true, true,
                getAuthorities(customerAccountDetailsEntity.getRole()));

        return userDetail;
    }

    private List<GrantedAuthority> getAuthorities(Integer role)
    {

        List<GrantedAuthority> authList = new ArrayList<GrantedAuthority>();
        if (role.intValue() == UserAuthorities.ADMIN.getRole())
        {
            authList.add(new SimpleGrantedAuthority("ROLE_USER"));
            authList.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }
        else
        {
            if (role.intValue() == UserAuthorities.USER.getRole())
            {
                authList.add(new SimpleGrantedAuthority("ROLE_USER"));
            }
        }
        return authList;
    }

}
