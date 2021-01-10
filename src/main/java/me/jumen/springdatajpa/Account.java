package me.jumen.springdatajpa;

import javax.persistence.*;
import java.util.Date;


@Entity(name = "Account") // 테이블 맵핑. name은 table이름
public class Account {  // User는 keyword이므로 되도록 사용하지 말것
    @Id // ID로 쓴다
    @GeneratedValue // 자동생성값, DB마다 생성전략이 다를 수 있
    private Long id;

    @Column(nullable = false, unique = true)// 생략되어있는거랑 마찬가이다. ddl-auto가 update인 경우 Column 속성을 변경하면 이미 만들어져 있기 때문에 오류가 난다. 그래서 보통 개발시 create가 간편하다.
    private String username;

    private String password;

    @Temporal(TemporalType.TIMESTAMP)
    private Date created = new Date();

    private String yes;

    @Transient  // 컬럼으로 맵핑을 안해준다. 순수히 객체로만 사용할때
    private String no;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
