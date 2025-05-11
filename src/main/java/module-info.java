module TimeBank {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires spring.context;
    requires spring.boot;
    requires spring.boot.autoconfigure;
    requires spring.beans;
    requires spring.core;
    requires static lombok;
    requires java.logging;
    requires java.sql;
    requires spring.jdbc;
    requires spring.aop;
    requires spring.data.jpa;
    requires spring.security.crypto;
    requires spring.context.support;
    requires jakarta.mail;
    requires jasypt.spring.boot;
    requires jakarta.annotation;
    requires jdk.jfr;
    requires spring.tx;


    // Apri tutti i pacchetti necessari
    opens org.fabiojava.timebank;
    opens org.fabiojava.timebank.infrastructure.adapters.database;
    opens org.fabiojava.timebank.infrastructure.adapters.repositories;
    opens org.fabiojava.timebank.domain.services;
    opens org.fabiojava.timebank.domain.ports.database;
    opens org.fabiojava.timebank.domain.model;
    opens org.fabiojava.timebank.gui.controllers;
    opens org.fabiojava.timebank.gui.services;
    opens org.fabiojava.timebank.gui.utils;
    opens org.fabiojava.timebank.infrastructure.config;
    opens org.fabiojava.timebank.domain.dto;
    opens org.fabiojava.timebank.domain.ports.repositories;
    opens org.fabiojava.timebank.infrastructure.adapters.database.adapter;
    opens org.fabiojava.timebank.infrastructure.adapters.database.specification;
}