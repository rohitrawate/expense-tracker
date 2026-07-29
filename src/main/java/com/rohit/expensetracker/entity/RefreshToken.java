package com.rohit.expensetracker.entity;

import com.rohit.expensetracker.common.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "refresh_tokens")
public class RefreshToken extends BaseEntity {
}