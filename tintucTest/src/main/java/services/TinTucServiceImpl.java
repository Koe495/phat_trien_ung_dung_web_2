package services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import thigk2.phannhattan.tintuc.entities.TinTuc;
import thigk2.phannhattan.tintuc.repository.TinTucRepository;

@Service
public class TinTucServiceImpl implements TinTucService {

    @Autowired
    private TinTucRepository repo;

	@Override
	public List<TinTuc> findAll() {
		return repo.findAll();
	}

	@Override
	public TinTuc save(TinTuc tinTuc) {
		return repo.save(tinTuc);
	}

	@Override
	public void delete(Long id) {
		repo.deleteById(id);
	}

	@Override
	public TinTuc findById(Long id) {
		return repo.findById(id).orElse(null);
	}

    
}