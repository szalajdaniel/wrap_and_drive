package wrap_and_drive.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

// UWAGA: Upewnij się, że poniższy import pasuje do Twojego folderu z modelami!
// Jeśli Twój folder to "Model" (wielką literą), to zostaw jak jest.
import wrap_and_drive.Model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByManufacturer(String manufacturer);
    List<Product> findByFinishType(String finishType);
}