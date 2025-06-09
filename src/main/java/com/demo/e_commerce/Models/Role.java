package com.demo.e_commerce.Models;


import java.util.Set;

import jakarta.persistence.*;

@Entity
public class Role {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer idRole;

  @Enumerated(EnumType.STRING)
  @Column(length = 20)
  private ERole name;

  public Role() {

  }
  
  public Role(Integer idRole, ERole name, Set<User> userRole) {
    this.idRole = idRole;
    this.name = name;
    this.userRole = userRole;
  }

  @ManyToMany
  private Set<User> userRole;
  public Role(ERole name) {
    this.name = name;
  }

  public Integer getIdRole() {
    return idRole;
  }

  public void setIdRole(Integer idRole) {
    this.idRole = idRole;
  }

  public ERole getName() {
    return name;
  }

  public void setName(ERole name) {
    this.name = name;
  }
  
  
}