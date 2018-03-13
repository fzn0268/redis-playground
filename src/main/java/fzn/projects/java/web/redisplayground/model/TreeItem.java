package fzn.projects.java.web.redisplayground.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.util.Map;

import static fzn.projects.java.web.redisplayground.model.TreeItem.REDIS_HASH;

@ToString(exclude = "payload")
@RedisHash(REDIS_HASH)
public class TreeItem {
    public static final String REDIS_HASH = "{TreeItem}";

    public static final String NAMESPACE_CHILDREN_ID_SET = "{TreeChildrenIdSet}";

    @Getter
    @Setter
    @Id
    private String id;

    @Getter
    @Setter
    @Indexed
    private String parentId;

    @Getter
    @Setter
    @Indexed
    private String name;

    @Getter
    @Setter
    private Map<String, String> payload;
}
