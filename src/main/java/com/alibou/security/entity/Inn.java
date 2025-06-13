package com.alibou.security.entity;

import com.alibou.security.entity.base.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "inn_ref")
@Getter
@Setter
public class Inn extends BaseEntity {
}
