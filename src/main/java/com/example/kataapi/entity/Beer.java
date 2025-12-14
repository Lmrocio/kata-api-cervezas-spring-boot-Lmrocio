package com.example.kataapi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "beers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Beer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "brewery_id", nullable = false)
    private Integer breweryId = 0;

    @Column(name = "name", nullable = false)
    private String name = "";

    @Column(name = "cat_id", nullable = false)
    private Integer catId = 0;

    @Column(name = "style_id", nullable = false)
    private Integer styleId = 0;

    @Column(name = "abv", nullable = false)
    private Double abv = 0.0;

    @Column(name = "ibu", nullable = false)
    private Double ibu = 0.0;

    @Column(name = "srm", nullable = false)
    private Double srm = 0.0;

    @Column(name = "upc", nullable = false)
    private Integer upc = 0;

    @Column(name = "filepath", nullable = false)
    private String filepath = "";

    @Column(name = "descript", nullable = false)
    private String descript = "";

    @Column(name = "add_user", nullable = false)
    private Integer addUser = 0;

    @Column(name = "last_mod", nullable = false)
    private java.sql.Timestamp lastMod;
}
