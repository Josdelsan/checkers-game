package springframework.project.util;

import java.security.SecureRandom;

public class StringGenerator {
    
    public static String gameCodeGenerator() {
        return secureAlphanumericStringGenerator(5).toUpperCase();
    }

    public static String playerTokenGenerator() {
        return secureAlphanumericStringGenerator(64);
    }

    private static String secureAlphanumericStringGenerator(int length) {
        int leftLimit = 48;
        int rightLimit = 122;
        SecureRandom secureRandom = new SecureRandom();

        String string = secureRandom.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        return string;
    }
}
