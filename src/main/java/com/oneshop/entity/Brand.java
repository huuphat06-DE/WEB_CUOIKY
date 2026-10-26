package com.oneshop.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * THÀNH VIÊN 1: Thực thể Thương hiệu mỹ phẩm (La Roche-Posay, Innisfree, CeraVe, MAC, Kiehl's...)
 */
@Entity
@Table(name = "brands")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Brand {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @Column(length = 100)
    private String slug;

    @Column(length = 100)
    private String country; // Pháp, Hàn Quốc, Mỹ, Nhật Bản...

    @Column(length = 500)
    private String logo;

    @Column(length = 500)
    private String description;

    public Brand(Long id, String name, String country, String logo, String description) {
        this.id = id;
        this.name = name;
        this.slug = name != null ? name.toLowerCase().replaceAll("[^a-z0-9]", "-") : "";
        this.country = country;
        this.logo = logo;
        this.description = description;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSlug() { return slug; }
    public void setSlug(String slug) { this.slug = slug; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public String getLogo() { return logo; }
    public void setLogo(String logo) { this.logo = logo; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
