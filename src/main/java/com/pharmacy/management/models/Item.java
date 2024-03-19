package com.pharmacy.management.models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "items", schema = "${myapp.schema.name}")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "manf_name", nullable = false)
    private String manfName;

    @Column(name = "generic_name")
    private String genericName;

    @Column(name = "sell_uom", nullable = false)
    private Integer sellUom;

    @Column(name = "uom_price", nullable = false)
    private Double uomPrice;

    @Column(name = "package_size", nullable = false)
    private Integer packageSize;

    @Column(name = "unique_identifier")
    private String uniqueIdentifier;

    @Column(name = "general_text")
    private String generalText;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @Column(name = "deleted_at")
    private Timestamp deletedAt;

    @Column(name = "soft_delete")
    private Boolean softDelete;

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL)
    private List<ItemTax> itemTaxes;
}