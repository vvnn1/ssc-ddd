package indi.melon.ssc.domain.common.utils;

import java.util.Objects;

/**
 * @author vvnn1
 * @since 2024/4/7 20:36
 */
public class StringUtils {
    public static boolean equals(String str1, String str2) {
        return Objects.equals(str1, str2);
    }

    public static boolean isBlank(String str) {
        return str == null || str.trim().isEmpty();
    }
}
