package com.example.projeto_so.entity;

public @interface Column {

    String name();

    boolean unique();

    boolean nullable();

}
