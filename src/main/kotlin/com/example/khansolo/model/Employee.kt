package com.example.khansolo.model

import javax.persistence.*
/**
 * Model Class for Employee
 */
@Entity
@Table(name = "employee")
class Employee(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id : Long = 0,
        val email : String = "",
        val firstname : String = "",
        val lastname : String = ""
){}