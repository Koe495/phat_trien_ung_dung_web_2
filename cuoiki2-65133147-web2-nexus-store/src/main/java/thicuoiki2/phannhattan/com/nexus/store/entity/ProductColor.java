package thicuoiki2.phannhattan.com.nexus.store.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "product_colors")
@Data
@NoArgsConstructor
public class ProductColor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "color_hex", nullable = false, length = 7)
    private String colorHex;

    @Column(name = "color_name", length = 50)
    private String colorName;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public String getColorHex() {
		return colorHex;
	}

	public void setColorHex(String colorHex) {
		this.colorHex = colorHex;
	}

	public String getColorName() {
		return colorName;
	}

	public void setColorName(String colorName) {
		this.colorName = colorName;
	}
}