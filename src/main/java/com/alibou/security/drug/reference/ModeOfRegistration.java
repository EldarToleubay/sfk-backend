package com.alibou.security.drug.reference;

import com.alibou.security.drug.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "mode_of_registration_ref")
@Getter
@Setter
public class ModeOfRegistration extends BaseEntity {
}
