package io.github.nhatbangle.sdp.product.util;

import jakarta.validation.constraints.NotNull;
import org.apache.commons.codec.digest.Crypt;
import org.springframework.validation.annotation.Validated;

@Validated
public class KeyEncryption {

    private static final String salt = "$5$super0pro0vip";

    public static String crypt(@NotNull String key) {
        var hashedVal = Crypt.crypt(key, salt);
        return hashedVal.substring(hashedVal.lastIndexOf("$") + 1);
    }

    public static boolean compare(@NotNull String keyToCheck, @NotNull String hashedValue) {
        var hashValWithSalt = salt + "$" + hashedValue;
        return hashValWithSalt.equals(Crypt.crypt(keyToCheck, hashValWithSalt));
    }

}
