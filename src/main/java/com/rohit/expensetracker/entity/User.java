package com.rohit.expensetracker.entity;

import com.rohit.expensetracker.common.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User extends BaseEntity {
}