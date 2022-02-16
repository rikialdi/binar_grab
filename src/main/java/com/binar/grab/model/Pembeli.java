package com.binar.grab.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.*;
import java.io.Serializable;

@Setter
@Getter
@Entity
@Table(name = "pembeli")
public class Pembeli implements Serializable
{
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nama", nullable = false, length = 45)
    private String nama;

    //step 1 one to one
    @OneToOne(mappedBy = "pembeli")
    private PembeliDetail pembeliDetail;


}
