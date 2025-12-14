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

    @Column(name = "name")
    private String name;

    @Column(name = "brewery_id")
    private Integer breweryId;

    @Column(name = "category_id")
    private Integer categoryId;

    @Column(name = "style_id")
    private Integer styleId;

    @Column(name = "abv")
    private Double abv;

    @Column(name = "ibu")
    private Double ibu;

    @Column(name = "description")
    private String description;

}

