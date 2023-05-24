package com.nataraj.currencyExchange.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Coin {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "coin_name")
    private String coinName;

    @Column(name = "coin_value")
    private BigDecimal coinValue;

    @Column(name = "count")
    private Long count;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "coin")
    @Fetch(FetchMode.SUBSELECT)
    private List<BillChange> billChange = new ArrayList<>();

}
