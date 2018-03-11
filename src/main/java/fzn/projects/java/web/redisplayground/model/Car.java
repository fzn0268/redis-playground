package fzn.projects.java.web.redisplayground.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.time.LocalDate;

@RedisHash("{Car}")
public class Car {
    @Getter
    @Setter
    @Id
    private String id;

    @Getter
    @Setter
    @Indexed
    private String brand;

    @Getter
    @Setter
    @Indexed
    private String model;

    @Getter
    @Setter
    @Indexed
    private LocalDate registrationDate;

    @Getter
    @Setter
    private double price;

    @Getter
    @Setter
    private String pageLink;

    @Getter
    @Setter
    @Indexed
    private LocalDate publishDate;

    @Getter
    @Setter
    @Indexed
    private boolean sold;

    public void update(Car car) {
        price = car.price;
        sold = car.sold;
    }
}
