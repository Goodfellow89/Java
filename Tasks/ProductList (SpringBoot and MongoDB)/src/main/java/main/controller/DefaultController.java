package main.controller;

import lombok.RequiredArgsConstructor;
import main.model.Product;
import main.model.ProductList;
import main.response.ProductListResponse;
import main.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class DefaultController {

    private final ProductService service;

    @GetMapping("/product")
    public ProductListResponse getAllProducts() {
        return service.getAllProducts();
    }

    @GetMapping("/list")
    public List<ProductList> getAllLists() {
        return service.getAllLists();
    }

    @GetMapping("/list/{ID}")
    public ResponseEntity<ProductListResponse> getList(@PathVariable("ID") long id) {
        ProductListResponse response = service.getList(id);
        if (response == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return new ResponseEntity<>(service.getList(id), HttpStatus.OK);
    }

    @PostMapping("/product")
    public void addProduct(Product product) {
        service.addNewProduct(product);
    }

    @PostMapping("/list")
    public void addList(ProductList list) {
        service.addNewList(list);
    }

    @PutMapping("/list/{ID}")
    public ResponseEntity<String> addProductToList(@PathVariable("ID") long listId, long id) {
        String response = service.addProductToList(listId, id);
        if (response == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/product/{ID}")
    public void deleteProduct(@PathVariable("ID") long id) {
        service.deleteProduct(id);
    }

    @DeleteMapping("/product")
    public void deleteAllProducts() {
        service.deleteAllProducts();
    }

    @DeleteMapping("/list/{ID}")
    public void deleteList(@PathVariable("ID") long id) {
        service.deleteList(id);
    }

    @DeleteMapping("/list")
    public void deleteAllLists() {
        service.deleteAllLists();
    }
}
