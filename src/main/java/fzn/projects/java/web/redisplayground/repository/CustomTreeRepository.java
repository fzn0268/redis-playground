package fzn.projects.java.web.redisplayground.repository;

import fzn.projects.java.web.redisplayground.model.TreeItem;

public interface CustomTreeRepository {
    void addItem(TreeItem item);

    void removeItem(TreeItem item);

    void removeItem(String id);

    void updateItem(TreeItem item);

    Iterable<TreeItem> findChildren(TreeItem parent);

    Iterable<TreeItem> findChildren(String id);

    TreeItem findRoot(TreeItem item);

    TreeItem findRoot(String id);
}
