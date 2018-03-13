package fzn.projects.java.web.redisplayground.repository.impl;

import fzn.projects.java.web.redisplayground.model.TreeItem;
import fzn.projects.java.web.redisplayground.repository.CrudTreeRepository;
import fzn.projects.java.web.redisplayground.repository.CustomTreeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
@Slf4j
public class CustomTreeRepositoryImpl implements CustomTreeRepository {
    private static final String SEPARATOR_NAMESPACE = ":";

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private CrudTreeRepository treeRepository;

    @Override
    public void addItem(TreeItem item) {
        Assert.notNull(item, "Tree item is null.");
        String id = UUID.randomUUID().toString();
        item.setId(id);
        String parentId = item.getParentId();
        if (parentId != null) {
            String parentKey = TreeItem.REDIS_HASH + SEPARATOR_NAMESPACE + parentId;
            if (stringRedisTemplate.hasKey(parentKey)) {
                stringRedisTemplate.opsForSet().add(TreeItem.NAMESPACE_CHILDREN_ID_SET + SEPARATOR_NAMESPACE + parentId, id);
            }
        }
        treeRepository.save(item);
    }

    @Override
    public void removeItem(TreeItem item) {
        Assert.notNull(item, "Tree item is null.");
        Assert.notNull(item.getId(), "Id of tree item is null.");
        String parentId = item.getParentId();
        if (parentId != null) {
            stringRedisTemplate.opsForSet().remove(TreeItem.NAMESPACE_CHILDREN_ID_SET + SEPARATOR_NAMESPACE + parentId,
                    item.getId());
        }
        stringRedisTemplate.delete(TreeItem.NAMESPACE_CHILDREN_ID_SET + SEPARATOR_NAMESPACE + item.getId());
        treeRepository.delete(item);
    }

    @Override
    public void removeItem(String id) {
        removeItem(findByIdOrThrow(id));
    }

    @Override
    public void updateItem(TreeItem item) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Iterable<TreeItem> findChildren(TreeItem parent) {
        String childrenIdSetKey = TreeItem.NAMESPACE_CHILDREN_ID_SET + SEPARATOR_NAMESPACE
                + parent.getId();
        Set<String> childrenIdSet = stringRedisTemplate.opsForSet().members(childrenIdSetKey);
        return treeRepository.findAllById(childrenIdSet);
    }

    @Override
    public Iterable<TreeItem> findChildren(String id) {
        return findChildren(findByIdOrThrow(id));
    }

    @Override
    public TreeItem findRoot(TreeItem item) {
        final int maxHeight = 10;
        TreeItem current = item;
        for (int height = 0; height < maxHeight; height++) {
            String parentId = current.getParentId();
            if (parentId == null) {
                return current;
            }
            Optional<TreeItem> itemOptional = treeRepository.findById(parentId);
            if (!itemOptional.isPresent()) {
                return current;
            }
            current = itemOptional.get();
        }
        return null;
    }

    @Override
    public TreeItem findRoot(String id) {
        return findRoot(findByIdOrThrow(id));
    }

    private TreeItem findByIdOrThrow(String id) {
        Optional<TreeItem> itemOptional = treeRepository.findById(id);
        if (!itemOptional.isPresent()) {
            throw new IllegalArgumentException("Could not find item.");
        }
        return itemOptional.get();
    }
}
