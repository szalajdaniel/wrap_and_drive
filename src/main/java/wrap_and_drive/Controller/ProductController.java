package wrap_and_drive.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wrap_and_drive.Repository.ProductRepository;
import wrap_and_drive.Model.Product;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    @Autowired
    private ProductRepository productRepository;

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(productRepository.findAll());
    }

    @GetMapping("/filter")
    public ResponseEntity<List<Product>> getProductsByManufacturer(@RequestParam String manufacturer) {
        return ResponseEntity.ok(productRepository.findByManufacturer(manufacturer));
    }
}