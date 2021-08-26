package br.com.santander.spring.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity(name = "TRANSACOES")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class Transacao implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    //@SequenceGenerator(sequenceName = "TRANSACOES_SEQ", allocationSize = 1, name = "TRANSACOES_SEQ")
    @Column(name = "ID_TRANSACAO")
    private Integer idTransacao;

    @Temporal(TemporalType.DATE)
    private Date dataTransacao;

    @Column(name = "TIPO_TRANSACAO")
    private String tipoTransacao;

    @Column(name = "VALOR")
    private Double valor;

    @JsonManagedReference
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_CLIENTE")
    private Cliente cliente;

    public Transacao(Date dataTransacao, String tipoTransacao, Double valor, Cliente cliente) {
        this.dataTransacao = dataTransacao;
        this.tipoTransacao = tipoTransacao;
        this.valor = valor;
        this.cliente = cliente;
    }


}
