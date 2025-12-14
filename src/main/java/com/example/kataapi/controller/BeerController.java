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

    // GET - Obtener todas las cervezas
    @GetMapping
    public ResponseEntity<List<Beer>> getAllBeers() {
        List<Beer> beers = beerRepository.findAll();
        return ResponseEntity.ok(beers);
    }

    // GET - Obtener una cerveza por ID
    @GetMapping("/{id}")
    public ResponseEntity<Beer> getBeerById(@PathVariable Integer id) {
        Optional<Beer> beer = beerRepository.findById(id);
        if (beer.isPresent()) {
            return ResponseEntity.ok(beer.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // POST - Crear una nueva cerveza
    @PostMapping
    public ResponseEntity<Beer> createBeer(@RequestBody Beer beer) {
        Beer savedBeer = beerRepository.save(beer);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedBeer);
    }

    // PUT - Actualizar una cerveza (total o parcial)
    @PutMapping("/{id}")
    public ResponseEntity<Beer> updateBeer(@PathVariable Integer id, @RequestBody Beer beerDetails) {
        Optional<Beer> optionalBeer = beerRepository.findById(id);

        if (optionalBeer.isPresent()) {
            Beer beer = optionalBeer.get();

            // Actualizar solo los campos que vienen en el request
            if (beerDetails.getName() != null) {
                beer.setName(beerDetails.getName());
            }
            if (beerDetails.getBreweryId() != null) {
                beer.setBreweryId(beerDetails.getBreweryId());
            }
            if (beerDetails.getCategoryId() != null) {
                beer.setCategoryId(beerDetails.getCategoryId());
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
            if (beerDetails.getDescription() != null) {
                beer.setDescription(beerDetails.getDescription());
            }

            Beer updatedBeer = beerRepository.save(beer);
            return ResponseEntity.ok(updatedBeer);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE - Eliminar una cerveza
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

