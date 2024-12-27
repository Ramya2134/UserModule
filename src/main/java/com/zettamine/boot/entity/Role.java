package com.zettamine.boot.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@Table(name = "roles")
@AllArgsConstructor
@RequiredArgsConstructor
public class Role{
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer roleId;
	
	private String roleName;
	
	private String roleDesc;
	
	@OneToMany(mappedBy = "role",cascade = CascadeType.ALL)
	private List<User> users;
	

}