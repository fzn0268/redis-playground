package fzn.projects.java.web.redisplayground;

import org.springframework.util.Assert;

public class Utils {
    public static String spliceRedisKey(String... item) {
        Assert.notEmpty(item, "At lease two key items.");
        final String separator = ":";
        if (item.length == 2) {
            return String.format("%s:%s", item[0], item[1]);
        }
        if (item.length == 3) {
            return String.format("%s:%s:%s", item[0], item[1], item[2]);
        }
        StringBuilder builder = new StringBuilder();
        int length = item.length;
        int lastColonAt = length - 1;
        for (int i = 0; i < item.length; i++) {
            if (item[i] == null) {
                continue;
            }
            builder.append(item[i]);
            if (i < lastColonAt) {
                builder.append(":");
            }
        }
        return builder.toString();
    }
}
