package kz.narxoz.springapp.repository;

import kz.narxoz.springapp.model.Flower;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface FlowerRepository extends JpaRepository<Flower, Long> {



}
