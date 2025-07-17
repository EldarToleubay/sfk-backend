package com.alibou.security.drug.reference;

import com.alibou.security.drug.BaseEntity;
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
