package ru.practicum.user;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.List;

@Repository
public class UserRepositoryImpl implements UserRepositoryCustom {

    private final UserRepository userRepository;

    public UserRepositoryImpl(@Lazy UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<UserShortWithIp> findAllByEmailContainingIgnoreCaseWithIP(String emailSearch) {
        return userRepository.findAllByEmailContainingIgnoreCase(emailSearch).stream()
                .map(userShort -> new UserShortWithIp(userShort, getServerIP(getEmailServer(emailSearch))))
                .toList();
    }

    private String getServerIP(String emailServer) {
        try{
            return InetAddress.getByName(emailServer).toString();
        } catch (UnknownHostException e) {
            return "127.0.0.1";
        }
    }

    private String getEmailServer(String email) {
        String[] parts = email.split("@");
        if (parts.length == 2) {
            return parts[1];
        }
        return "";
    }

}
