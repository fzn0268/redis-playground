package fzn.projects.java.web.redisplayground.repository;

import fzn.projects.java.web.redisplayground.model.Car;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarRepository extends CrudRepository {
    List<Car> findByBrand(String brand);

    List<Car> findByBrandAndModel(String brand, String model);
}
