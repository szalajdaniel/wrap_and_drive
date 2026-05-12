package wrap_and_drive.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import wrap_and_drive.Model.Product;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByManufacturer(String manufacturer);
    List<Product> findByFinishType(String finishType); // To tu Spring szukał pola 'finishType'
}