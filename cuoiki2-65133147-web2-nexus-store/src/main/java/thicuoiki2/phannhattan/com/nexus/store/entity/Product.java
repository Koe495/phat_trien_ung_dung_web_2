package thicuoiki2.phannhattan.com.nexus.store.entity;

import java.util.List;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "products")
@Data // Lombok tự động tạo Getter, Setter, toString...
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Khóa ngoại liên kết với bảng Category
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(nullable = false, unique = true, length = 150)
    private String slug;

    @Column(length = 255)
    private String spec;

    @Column(nullable = false)
    private Double price;

    @Column(length = 50)
    private String badge;

    @Column(name = "thumbnail_bg", length = 255)
    private String thumbnailBg;

    @Column(length = 20)
    private String status = "active"; // Mặc định là active
    
 // Thêm thuộc tính này vào cuối lớp Product.java
    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ProductColor> colors;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSlug() {
		return slug;
	}

	public void setSlug(String slug) {
		this.slug = slug;
	}

	public String getSpec() {
		return spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getBadge() {
		return badge;
	}

	public void setBadge(String badge) {
		this.badge = badge;
	}

	public String getThumbnailBg() {
		return thumbnailBg;
	}

	public void setThumbnailBg(String thumbnailBg) {
		this.thumbnailBg = thumbnailBg;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<ProductColor> getColors() {
		return colors;
	}

	public void setColors(List<ProductColor> colors) {
		this.colors = colors;
	}
}