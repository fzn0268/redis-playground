package fzn.projects.java.web.redisplayground.endpoint;

import fzn.projects.java.web.redisplayground.model.Car;
import fzn.projects.java.web.redisplayground.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisKeyValueTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import java.util.List;

@Component
@Path("car")
public class CarEndpoint {

    @Autowired
    private CarRepository repository;

    @Autowired
    private RedisTemplate template;

    @Autowired
    private RedisKeyValueTemplate keyValueTemplate;

    @POST
    public void postCars(List<Car> cars) {
        repository.saveAll(cars);
    }

    @PUT
    public void modifyCars(List<Car> cars) {
        for (Car car : cars)
        {
            List<Car> byBrandAndModel = repository.findByBrandAndModel(car.getBrand(), car.getModel());
            byBrandAndModel.forEach((found) -> {
                found.update(car);
                repository.save(found);
            });
        }
    }

    @GET
    @Path("{brand}/{model}")
    public List<Car> getCar(@PathParam("brand") String brand, @PathParam("model") String model) {
        return repository.findByBrandAndModel(brand, model);
    }

    @GET
    public String message() {
        return "Hello";
    }
}
