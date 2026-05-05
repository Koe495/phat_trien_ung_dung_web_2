package controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import services.TinTucService;
import thigk2.phannhattan.tintuc.entities.TinTuc;

@RestController
@RequestMapping("/api/tintuc")
public class TinTucRestController {

    @Autowired
    private TinTucService service;

    @GetMapping
    public List<TinTuc> getAll() {
        return service.findAll();
    }

    @PostMapping
    public TinTuc create(@RequestBody TinTuc tinTuc) {
        return service.save(tinTuc);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}