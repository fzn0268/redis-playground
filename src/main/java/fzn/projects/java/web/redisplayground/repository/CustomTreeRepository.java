package fzn.projects.java.web.redisplayground.repository;

import fzn.projects.java.web.redisplayground.model.TreeItem;

import java.util.List;

public interface CustomTreeRepository {
    void addItem(TreeItem item);

    void removeItem(TreeItem item);

    void removeItem(String id);

    void updateItem(TreeItem item);

    Iterable<TreeItem> findChildren(TreeItem parent);

    Iterable<TreeItem> findChildren(String id);

    String findRootKey(TreeItem item);

    String findRootKey(String id);
}
