package com.example.kataapi.controller;

import com.example.kataapi.entity.Beer;
import com.example.kataapi.repository.BeerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/beers")
public class BeerController {

    @Autowired
    private BeerRepository beerRepository;

    @GetMapping
    public ResponseEntity<List<Beer>> getAllBeers() {
        List<Beer> beers = beerRepository.findAll();
        return ResponseEntity.ok(beers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Beer> getBeerById(@PathVariable Integer id) {
        Optional<Beer> beer = beerRepository.findById(id);
        if (beer.isPresent()) {
            return ResponseEntity.ok(beer.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<?> createBeer(@RequestBody Beer beer) {
        try {

            if (beer.getName() == null || beer.getName().trim().isEmpty()) {
                beer.setName("");
            }
            if (beer.getBreweryId() == null) {
                beer.setBreweryId(0);
            }
            if (beer.getCatId() == null) {
                beer.setCatId(0);
            }
            if (beer.getStyleId() == null) {
                beer.setStyleId(0);
            }
            if (beer.getAbv() == null) {
                beer.setAbv(0.0);
            }
            if (beer.getIbu() == null) {
                beer.setIbu(0.0);
            }
            if (beer.getSrm() == null) {
                beer.setSrm(0.0);
            }
            if (beer.getUpc() == null) {
                beer.setUpc(0);
            }
            if (beer.getFilepath() == null) {
                beer.setFilepath("");
            }
            if (beer.getDescript() == null) {
                beer.setDescript("");
            }
            if (beer.getAddUser() == null) {
                beer.setAddUser(0);
            }

            // Establecer lastMod con la hora actual si no está establecido
            if (beer.getLastMod() == null) {
                beer.setLastMod(new java.sql.Timestamp(System.currentTimeMillis()));
            }

            Beer savedBeer = beerRepository.save(beer);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedBeer);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error creating beer: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Beer> updateBeer(@PathVariable Integer id, @RequestBody Beer beerDetails) {
        Optional<Beer> optionalBeer = beerRepository.findById(id);

        if (optionalBeer.isPresent()) {
            Beer beer = optionalBeer.get();

            if (beerDetails.getName() != null) {
                beer.setName(beerDetails.getName());
            }
            if (beerDetails.getBreweryId() != null) {
                beer.setBreweryId(beerDetails.getBreweryId());
            }
            if (beerDetails.getCatId() != null) {
                beer.setCatId(beerDetails.getCatId());
            }
            if (beerDetails.getStyleId() != null) {
                beer.setStyleId(beerDetails.getStyleId());
            }
            if (beerDetails.getAbv() != null) {
                beer.setAbv(beerDetails.getAbv());
            }
            if (beerDetails.getIbu() != null) {
                beer.setIbu(beerDetails.getIbu());
            }
            if (beerDetails.getDescript() != null) {
                beer.setDescript(beerDetails.getDescript());
            }
            if (beerDetails.getSrm() != null) {
                beer.setSrm(beerDetails.getSrm());
            }
            if (beerDetails.getUpc() != null) {
                beer.setUpc(beerDetails.getUpc());
            }
            if (beerDetails.getFilepath() != null) {
                beer.setFilepath(beerDetails.getFilepath());
            }
            if (beerDetails.getAddUser() != null) {
                beer.setAddUser(beerDetails.getAddUser());
            }

            beer.setLastMod(new java.sql.Timestamp(System.currentTimeMillis()));

            Beer updatedBeer = beerRepository.save(beer);
            return ResponseEntity.ok(updatedBeer);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBeer(@PathVariable Integer id) {
        Optional<Beer> beer = beerRepository.findById(id);

        if (beer.isPresent()) {
            beerRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}

