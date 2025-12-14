package com.example.kataapi.controller;

import com.example.kataapi.entity.Style;
import com.example.kataapi.repository.StyleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/styles")
public class StyleController {

    @Autowired
    private StyleRepository styleRepository;

    @GetMapping
    public ResponseEntity<List<Style>> getAllStyles() {
        List<Style> styles = styleRepository.findAll();
        return ResponseEntity.ok(styles);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Style> getStyleById(@PathVariable Integer id) {
        Optional<Style> style = styleRepository.findById(id);
        if (style.isPresent()) {
            return ResponseEntity.ok(style.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}

