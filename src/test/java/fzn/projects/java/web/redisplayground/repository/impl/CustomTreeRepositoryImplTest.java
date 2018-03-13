package fzn.projects.java.web.redisplayground.repository.impl;

import fzn.projects.java.web.redisplayground.model.TreeItem;
import fzn.projects.java.web.redisplayground.repository.CrudTreeRepository;
import fzn.projects.java.web.redisplayground.repository.CustomTreeRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Set;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CustomTreeRepositoryImplTest {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private CrudTreeRepository treeRepository;

    @Autowired
    private CustomTreeRepository customTreeRepository;

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
        Set<String> keys = stringRedisTemplate.keys("*");
        stringRedisTemplate.delete(keys);
    }

    @Test
    public void addItem() {
    }

    @Test
    public void removeItem() {
    }

    @Test
    public void findChildren() {
    }

    @Test
    public void findRoot() {
        TreeItem root = new TreeItem();
        root.setId(UUID.randomUUID().toString());
        root.setName("Root");
        treeRepository.save(root);
        TreeItem last;
        TreeItem child = new TreeItem();
        child.setId(UUID.randomUUID().toString());
        child.setName("deep1");
        child.setParentId(root.getId());
        treeRepository.save(child);
        last = child;
        child = new TreeItem();
        child.setId(UUID.randomUUID().toString());
        child.setName("deep2-1");
        child.setParentId(last.getId());
        treeRepository.save(child);
        child = new TreeItem();
        child.setId(UUID.randomUUID().toString());
        child.setName("deep2-2");
        child.setParentId(last.getId());
        treeRepository.save(child);
        last = child;
        child = new TreeItem();
        child.setId(UUID.randomUUID().toString());
        child.setName("deep3");
        child.setParentId(last.getId());
        treeRepository.save(child);
        TreeItem actual = customTreeRepository.findRoot(child);
        assertEquals("Root", actual.getName());
    }
}