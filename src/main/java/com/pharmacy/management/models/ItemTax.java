package com.pharmacy.management.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "item_taxes", schema = "${myapp.schema.name}")
public class ItemTax {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "tax_id", nullable = false)
    private Tax tax;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @Column(name = "deleted_at")
    private Timestamp deletedAt;

    @Column(name = "soft_delete", nullable = false, columnDefinition = "boolean default false")
    private Boolean softDelete;
}