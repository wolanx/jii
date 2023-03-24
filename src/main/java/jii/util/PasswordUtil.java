package jii.util;

import org.springframework.util.DigestUtils;

import java.util.UUID;

/**
 * @author admin BCryptPasswordEncoder
 */
public class PasswordUtil {

    public static String encodeTwinSalt(String password, String salt) {
        return DigestUtils.md5DigestAsHex((DigestUtils.md5DigestAsHex(password.getBytes()) + salt).getBytes());
    }

    public static String genRandomSalt() {
        // abc123
        return UUID.randomUUID().toString().substring(0, 6);
    }

    public static void main(String[] args) {
        // admin
        // 86f3059b228c8acf99e69734b6bb32cc
        String password = "admin";
        String salt = "admin";
        String pwd = encodeTwinSalt(password, salt);
        System.out.println(pwd);
    }

}
