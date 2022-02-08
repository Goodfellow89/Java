package main.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import main.model.Product;

import java.util.List;

@Getter
@Setter
public class ProductListResponse {

    String name;
    List<Product> product;

    @JsonProperty("Total kcal")
    int totalKcal;
}
