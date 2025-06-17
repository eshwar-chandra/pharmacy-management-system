package com.pharmacy.management.sale; // Updated package

import com.pharmacy.management.customer.Customer; // Updated import
import com.pharmacy.management.user.User; // Updated import
import com.pharmacy.management.sale.SalesItem; // Self-reference for List type

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
@Builder
public class Sale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sale_id")
    private Long saleId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Column(name = "total_amount", nullable = false)
    private BigDecimal totalAmount;

    @Column(name = "sale_date")
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

    public void addSalesItem(SalesItem item) {
        salesItems.add(item);
        item.setSale(this);
    }

    public void removeSalesItem(SalesItem item) {
        salesItems.remove(item);
        item.setSale(null);
    }
}
