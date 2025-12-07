package com.arcbank.login.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "LoginCajero")
public class LoginCajero {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdLogin")
    private Integer idLogin;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IdCajero", nullable = false)
    private Cajero cajero;

    @Column(name = "FechaHora", nullable = false)
    private LocalDateTime fechaHora = LocalDateTime.now();
}