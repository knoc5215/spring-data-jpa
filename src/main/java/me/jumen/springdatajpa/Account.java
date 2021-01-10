package me.jumen.springdatajpa;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;


@Entity(name = "Account") // 테이블 맵핑. name은 table이름
@Getter
@Setter
@ToString
public class Account {  // User는 keyword이므로 되도록 사용하지 말것
    /*
     * @Id : ID로 쓴다 (식별자)
     * @GeneratedValue :  자동생성값, DB마다 생성전략이 다를 수 있다.
     * */

    @Id
    @GeneratedValue
    private Long id;

    /*
     * @Column
     * 생략되어있는거랑 마찬가이다.
     * ddl-auto가 update인 경우 Column 속성을 변경하면 이미 만들어져 있기 때문에 오류가 난다.
     * 그래서 보통 개발시 create가 간편하다.
     * */

    @Column(nullable = false, unique = true)
    private String username;

    private String password;

    /*
     * 한 Account는 여러 study를 만들 수 이있다.
     * 끝쪽이 Many면 Collection이다.
     * */

    @OneToMany(mappedBy = "owner")
    private Set<Study> studies = new HashSet<>();

    @Temporal(TemporalType.TIMESTAMP)
    private Date created = new Date();

    private String yes;

    /*
     * @Transient
     * 컬럼으로 맵핑을 안해준다. 순수히 객체로만 사용할때 명시한다.
     * */

    @Transient
    private String no;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "street", column = @Column(name = "home_street"))
    })
    private Address address;


    public void addStudy(Study study) {
        this.getStudies().add(study);    // optional이지만 객체지향적으로 서로에 대한 관계를 넣어줄 것
        study.setOwner(this);            // 관계의 주인(study)에 설정을 해줘야 FK가 만들어진다.
    }


    public void removeStudy(Study study) {
        this.getStudies().remove(study);    // optional이지만 객체지향적으로 서로에 대한 관계를 넣어줄 것
        study.setOwner(null);
    }
}
