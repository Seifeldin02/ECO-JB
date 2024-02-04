package service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import repository.StaffRepository;
import repository.UserRepository;
import model.Staff;
import model.User;
import java.util.ArrayList;
import java.util.List;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StaffRepository staffRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        Staff staff = staffRepository.findByEmail(email);

        if (user == null && staff == null) {
            throw new UsernameNotFoundException("User not found");
        }

        String password;
        List<GrantedAuthority> authorities = new ArrayList<>();

        if (user != null) {
            password = user.getPassword();
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        } else {
            password = staff.getPassword();
            authorities.add(new SimpleGrantedAuthority("ROLE_STAFF"));
        }

        return new org.springframework.security.core.userdetails.User(email, password, authorities);
    }
}