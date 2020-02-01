package cn.cloudbed.mylibrary.framework.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Created by mrt on 2018/5/22.
 */

public class BCryptUtil {

    /**
     *  Implementation of PasswordEncoder that uses the BCrypt strong hashing function. Clients
     * can optionally supply a "strength" (a.k.a. log rounds in BCrypt) and a SecureRandom
     * instance. The larger the strength parameter the more work will have to be done
     * (exponentially) to hash the passwords. The default value is 10.
     *
     * 使用BCrypt强哈希方法实现了PasswordEncoder接口。
     * 客户端可以可选的提供一个 强度（或者 ）和一个加密随机实例。
     * 强度参数越大，加密需要的时间就更长，默认这个强度参数值为10.
     *
     */
    public static String encode(String password) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashPass = passwordEncoder.encode(password);
        return hashPass;
    }


    public static boolean matches(String password, String hashPass) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        boolean f = passwordEncoder.matches(password, hashPass);
        return f;
    }
}
