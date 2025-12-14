package com.example.kataapi.controller;

import com.example.kataapi.entity.Brewery;
import com.example.kataapi.repository.BreweryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/breweries")
public class BreweryController {

    @Autowired
    private BreweryRepository breweryRepository;

    @GetMapping
    public ResponseEntity<List<Brewery>> getAllBreweries() {
        List<Brewery> breweries = breweryRepository.findAll();
        return ResponseEntity.ok(breweries);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Brewery> getBreweryById(@PathVariable Integer id) {
        Optional<Brewery> brewery = breweryRepository.findById(id);
        if (brewery.isPresent()) {
            return ResponseEntity.ok(brewery.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}

