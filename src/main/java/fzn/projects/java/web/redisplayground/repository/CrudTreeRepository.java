package fzn.projects.java.web.redisplayground.repository;

import fzn.projects.java.web.redisplayground.model.TreeItem;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CrudTreeRepository extends CrudRepository<TreeItem, String> {
    List<TreeItem> findByParentId(String parentId);

    List<TreeItem> findByParentIdAndName(String parentId, String name);
}
