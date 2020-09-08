package pw.cdmi.wishlist.service;

import java.util.List;

import pw.cdmi.wishlist.model.ProductStatus;
import pw.cdmi.wishlist.model.entities.Product;

public interface ProductService {

    public Product createProduct(Product product);

    public Product updateProduct(Product product);

    public List<Product> getProductList();

    public List<Product> getInProgressProductList();

    public Product getProductById(String id);

    public Product getProductByIdAndStatus(String id, ProductStatus productStatus);

    public Boolean updateOnlookerNumber(String productId);

    public Boolean updateParticipantNumber(String productId);
}
