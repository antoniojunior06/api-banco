package br.ada.caixa.entity.conta;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@Getter
@Setter
@DiscriminatorValue("CI")
public class ContaInvestimento extends Conta {



}
