package services;

import java.util.List;

import thigk2.phannhattan.tintuc.entities.TinTuc;

public interface TinTucService {
    List<TinTuc> findAll();
    TinTuc save(TinTuc tinTuc);
    void delete(Long id);
    TinTuc findById(Long id);
}