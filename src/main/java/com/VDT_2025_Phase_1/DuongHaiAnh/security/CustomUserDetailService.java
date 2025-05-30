package com.VDT_2025_Phase_1.DuongHaiAnh.security;

import com.VDT_2025_Phase_1.DuongHaiAnh.entity.AuthAccount;
import com.VDT_2025_Phase_1.DuongHaiAnh.repository.AuthAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CustomUserDetailService implements UserDetailsService {

    @Autowired
    private AuthAccountRepository authAccountRepository;

    @Override
    public UserDetails loadUserByUsername(String account) throws UsernameNotFoundException {
        AuthAccount authAccount = authAccountRepository.findByAccount(account);
        if (authAccount == null) {
            throw new UsernameNotFoundException("User is not exist");
        }
        // First we init role list = [];
        return new User(authAccount.getAccount(), authAccount.getPassword(), new ArrayList<>());
    }
}
