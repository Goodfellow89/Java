package main.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter
@Setter
@Document(collection = "List")
@AllArgsConstructor
public class ProductList {

    @Id
    private long id;
    private String name;
    private List<Product> products;
}
