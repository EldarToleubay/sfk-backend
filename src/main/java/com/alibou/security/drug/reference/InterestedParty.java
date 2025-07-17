package com.alibou.security.drug.reference;

import com.alibou.security.drug.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "interested_party_ref")
@Getter
@Setter
public class InterestedParty extends BaseEntity {
}
