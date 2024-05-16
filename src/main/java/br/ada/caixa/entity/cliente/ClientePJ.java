package br.ada.caixa.entity.cliente;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Getter
@Setter
@Entity
@DiscriminatorValue("PJ")
public class ClientePJ extends Cliente {

    private String cnpj;
    private String nomeFantasia;
    private String razaoSocial;


}
