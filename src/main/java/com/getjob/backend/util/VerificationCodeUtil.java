package com.getjob.backend.util;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class VerificationCodeUtil {

    private final Map<String, CodeInfo> codeStorage = new ConcurrentHashMap<>();
    private static final Random random = new Random();

    private static class CodeInfo {
        String code;
        String email;
        long expireTime;
        String verificationToken;

        CodeInfo(String code, String email, long expireTime) {
            this.code = code;
            this.email = email;
            this.expireTime = expireTime;
            this.verificationToken = generateToken();
        }

        private String generateToken() {
            return "verification_token_" + System.currentTimeMillis() + "_" + random.nextInt(10000);
        }
    }

    public String generateCode(String email) {
        String code = String.format("%04d", random.nextInt(1000000));
        long expireTime = System.currentTimeMillis() + 300000; // 5分钟
        codeStorage.put(email, new CodeInfo(code, email, expireTime));
        return code;
    }

    public boolean verifyCode(String email, String code) {
        CodeInfo info = codeStorage.get(email);
        if (info == null) {
            return false;
        }
        if (System.currentTimeMillis() > info.expireTime) {
            codeStorage.remove(email);
            return false;
        }
        return info.code.equals(code);
    }

    public String getVerificationToken(String email) {
        CodeInfo info = codeStorage.get(email);
        if (info == null) {
            return null;
        }
        if (System.currentTimeMillis() > info.expireTime) {
            codeStorage.remove(email);
            return null;
        }
        return info.verificationToken;
    }

    public boolean verifyToken(String email, String token) {
        CodeInfo info = codeStorage.get(email);
        if (info == null) {
            return false;
        }
        if (System.currentTimeMillis() > info.expireTime) {
            codeStorage.remove(email);
            return false;
        }
        return info.verificationToken.equals(token);
    }

    public void removeCode(String email) {
        codeStorage.remove(email);
    }

    public long getRetryAfter(String email) {
        CodeInfo info = codeStorage.get(email);
        if (info == null) {
            return 0;
        }
        // 简单实现：如果存在则返回60秒
        return 60;
    }
}

