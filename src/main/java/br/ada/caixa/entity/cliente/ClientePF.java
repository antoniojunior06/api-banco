package br.ada.caixa.entity.cliente;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Getter
@Setter
@Entity
@DiscriminatorValue("PF")
public class ClientePF extends Cliente {

    private String cpf;
    private String nome;


}
