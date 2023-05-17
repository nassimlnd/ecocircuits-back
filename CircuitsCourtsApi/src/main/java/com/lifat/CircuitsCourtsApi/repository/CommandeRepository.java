package com.lifat.CircuitsCourtsApi.repository;

import com.lifat.CircuitsCourtsApi.model.Commande;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommandeRepository extends CrudRepository<Commande, Long> {

    Iterable<Commande> findByIdClient(Long id);

    Optional<Commande> findAllCommandeById(Long idCommande);

    @Query(value = "SELECT * FROM Commandes c INNER JOIN commande_details cd ON c.id = cd.id_commande INNER JOIN commande_producteur cp ON cd.id = cp.id_commande_details WHERE cp.id_producteur = :idProd", nativeQuery = true)
    Iterable<Commande> findAllCommandesByProducteur(@Param("idProd")Long idProd);




}
