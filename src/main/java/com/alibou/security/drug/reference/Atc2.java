package com.alibou.security.drug.reference;

import com.alibou.security.drug.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "atc2_ref")
@Getter
@Setter
public class Atc2 extends BaseEntity {
}
