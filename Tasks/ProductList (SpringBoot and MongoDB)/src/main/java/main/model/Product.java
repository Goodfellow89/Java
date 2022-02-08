package main.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "Product")
@AllArgsConstructor
public class Product {

    @Id
    private long id;
    private String name;
    private String description;
    private int kcal;
}
