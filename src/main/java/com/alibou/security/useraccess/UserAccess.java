package com.alibou.security.useraccess;

import com.alibou.security.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "user_access")
@Getter
@Setter
public class UserAccess {
    @Id
    @GeneratedValue
    private Long id;

    private String refType;     // "ATC1", "INN", "TradeName"
    private Long refId;         // id записи из соответствующего справочника

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
