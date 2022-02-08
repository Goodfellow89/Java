package main.service;

import lombok.RequiredArgsConstructor;
import main.model.Product;
import main.model.ProductList;
import main.repositiry.ListRepository;
import main.repositiry.ProductRepository;
import main.response.ProductListResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ListRepository listRepository;
    private final ProductRepository productRepository;

    public void addNewProduct(Product product) {
        productRepository.insert(product);
    }

    public void addNewList(ProductList list) {
        listRepository.insert(list);
    }

    public String addProductToList(long listId, long productId) {
        ProductList list = listRepository.findById(listId).orElse(null);
        Product product = productRepository.findById(productId).orElse(null);
        if (list == null || product == null) {
            return null;
        }
        List<Product> productList = list.getProducts();

        if (productList == null) {
            productList = new ArrayList<>();
        }
        productList.add(product);
        list.setProducts(productList);
        listRepository.save(list);
        return "Success";
    }

    public ProductListResponse getAllProducts() {
        ProductListResponse response = new ProductListResponse();
        response.setName("Products");
        response.setProduct(productRepository.findAll());
        return response;
    }

    public List<ProductList> getAllLists() {
        return listRepository.findAll();
    }

    public ProductListResponse getList(long id) {
        ProductListResponse response = new ProductListResponse();
        ProductList list = listRepository.findById(id).orElse(null);
        if (list == null) {
            return null;
        }
        response.setName(list.getName());
        response.setProduct(list.getProducts());
        if (!list.getProducts().isEmpty()) {
            response.setTotalKcal(list.getProducts().stream().mapToInt(Product::getKcal).sum());
        }
        return response;
    }

    public void deleteProduct(long id) {
        productRepository.deleteById(id);
    }

    public void deleteAllProducts() {
        productRepository.deleteAll();
    }

    public void deleteList(long id) {
        listRepository.deleteById(id);
    }

    public void deleteAllLists() {
        listRepository.deleteAll();
    }
}
