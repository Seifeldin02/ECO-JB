package service;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.ArrayList;
import repository.StaffRepository;
import model.Staff;

@Service
public class StaffDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private StaffRepository staffRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Staff staff = staffRepository.findByEmail(email);
        if (staff == null) {
            throw new UsernameNotFoundException("Staff not found");
        }
        return new org.springframework.security.core.userdetails.User(staff.getEmail(), staff.getPassword(), new ArrayList<>());
    }
}