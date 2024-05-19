package br.ada.caixa.repository;

import br.ada.caixa.entity.cliente.Cliente;
import br.ada.caixa.entity.cliente.ClientePF;
import br.ada.caixa.entity.cliente.ClientePJ;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, String> {

    @Query("select c from ClientePF c")
    List<ClientePF> findAllPF();

    @Query("select c from ClientePJ c")
    List<ClientePJ> findAllPJ();

    @Query("select c from ClientePF c where (:nome is null or lower(c.nome) like %:nome%)")
    List<ClientePF> findAllByName(@Param("nome") String nome);

   Optional<Cliente> findByDocumento(String documento);

    @Query("select c from ClientePJ c where (:razaoSocial is null or lower(c.razaoSocial) like %:razaoSocial%)")
    List<ClientePJ> findAllByRazaoSocial(@Param("razaoSocial") String razaoSocial);


}
