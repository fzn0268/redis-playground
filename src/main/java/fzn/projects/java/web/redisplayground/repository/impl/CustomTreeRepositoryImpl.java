package fzn.projects.java.web.redisplayground.repository.impl;

import fzn.projects.java.web.redisplayground.Utils;
import fzn.projects.java.web.redisplayground.model.TreeItem;
import fzn.projects.java.web.redisplayground.repository.CustomTreeRepository;
import fzn.projects.java.web.redisplayground.repository.CrudTreeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Supplier;

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
            String parentKey = item.getParentNameSpace() + SEPARATOR_NAMESPACE + parentId;
            if (stringRedisTemplate.hasKey(parentKey)) {
                stringRedisTemplate.opsForSet().add(TreeItem.TREE_CHILDREN_ID_SET + SEPARATOR_NAMESPACE + parentKey, id);
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
            String parentKey = item.getParentNameSpace() + SEPARATOR_NAMESPACE + parentId;
            stringRedisTemplate.opsForSet().remove(TreeItem.TREE_CHILDREN_ID_SET + SEPARATOR_NAMESPACE + parentKey,
                    item.getId());
        }
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
        String childrenIdSetKey = TreeItem.TREE_CHILDREN_ID_SET + SEPARATOR_NAMESPACE
                + parent.getParentNameSpace() + SEPARATOR_NAMESPACE + parent.getId();
        Set<String> childrenIdSet = stringRedisTemplate.opsForSet().members(childrenIdSetKey);
        return treeRepository.findAllById(childrenIdSet);
    }

    @Override
    public Iterable<TreeItem> findChildren(String id) {
        return findChildren(findByIdOrThrow(id));
    }

    @Override
    public String findRootKey(TreeItem item) {
        TreeItem nearestRootItem = item;
        String rootNameSpace = item.getParentNameSpace();
        String rootId = item.getParentId();
        if (!TreeItem.REDIS_HASH.equals(rootNameSpace) || rootId == null) {
            return rootNameSpace + SEPARATOR_NAMESPACE + nearestRootItem.getId();
        }
        Optional<TreeItem> itemOptional = treeRepository.findById(rootId);
        if (!itemOptional.isPresent()) {
            return rootNameSpace + SEPARATOR_NAMESPACE + nearestRootItem.getId();
        }
        nearestRootItem = itemOptional.get();
        rootNameSpace = nearestRootItem.getParentNameSpace();
        rootId = nearestRootItem.getParentId();
        for (; TreeItem.REDIS_HASH.equals(rootNameSpace) && rootId != null;
             itemOptional = treeRepository.findById(rootId)) {
            if (itemOptional.isPresent()) {
                nearestRootItem = itemOptional.get();
                rootNameSpace = nearestRootItem.getParentNameSpace();
                rootId = nearestRootItem.getParentId();
                if (TreeItem.REDIS_HASH.equals(rootNameSpace)) {
                    if (rootId == null) {
                        rootId = nearestRootItem.getId();
                    }
                    break;
                }
            }
            itemOptional = treeRepository.findById(rootId);
        }
        return rootNameSpace + SEPARATOR_NAMESPACE + rootId;
    }

    @Override
    public String findRootKey(String id) {
        return findRootKey(findByIdOrThrow(id));
    }

    private TreeItem findByIdOrThrow(String id) {
        Optional<TreeItem> itemOptional = treeRepository.findById(id);
        if (!itemOptional.isPresent()) {
            throw new IllegalArgumentException("Could not find item.");
        }
        return itemOptional.get();
    }
}
