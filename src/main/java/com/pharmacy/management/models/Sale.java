package com.pharmacy.management.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "sales")
@AllArgsConstructor
@NoArgsConstructor
@Where(clause = "soft_delete = false")
public class Sale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sale_id")
    private Long saleId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id") // Nullable if sale can be anonymous
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id") // Nullable if sale can be to a walk-in customer
    private Customer customer;

    @Column(name = "total_amount", nullable = false)
    private BigDecimal totalAmount;

    @Column(name = "sale_date") // Defaults to CURRENT_TIMESTAMP in DB schema
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp saleDate;

    @OneToMany(mappedBy = "sale", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<SalesItem> salesItems = new ArrayList<>();

    @Column(name = "created_at", updatable = false)
    private Timestamp createdAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @Column(name = "soft_delete")
    private boolean softDelete = false;

    @PrePersist
    protected void onCreate() {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        createdAt = now;
        updatedAt = now;
        if (saleDate == null) {
            saleDate = now;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Timestamp(System.currentTimeMillis());
    }

    // Helper methods for bidirectional association
    public void addSalesItem(SalesItem item) {
        salesItems.add(item);
        item.setSale(this);
    }

    public void removeSalesItem(SalesItem item) {
        salesItems.remove(item);
        item.setSale(null);
    }
}
