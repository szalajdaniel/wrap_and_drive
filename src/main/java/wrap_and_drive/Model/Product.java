package wrap_and_drive.Model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private BigDecimal price;
    private String manufacturer;

    @Column(name = "finish_type") // Musi być finish_type, bo tak daliśmy w XML
    private String finishType;

    @Column(name = "image_url")
    private String imageUrl;

    public Product() {}

    public Product(String name, BigDecimal price, String manufacturer, String finishType, String imageUrl) {
        this.name = name;
        this.price = price;
        this.manufacturer = manufacturer;
        this.finishType = finishType;
        this.imageUrl = imageUrl;
    }

    // Pamiętaj o wszystkich getterach i setterach dla KAŻDEGO pola!
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public String getManufacturer() { return manufacturer; }
    public void setManufacturer(String manufacturer) { this.manufacturer = manufacturer; }
    public String getFinishType() { return finishType; }
    public void setFinishType(String finishType) { this.finishType = finishType; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
}