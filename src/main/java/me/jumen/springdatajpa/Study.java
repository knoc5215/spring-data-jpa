package me.jumen.springdatajpa;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
@Getter
@Setter
@ToString(exclude = "owner")    // 상호참조 스택오버플로우 방지
public class Study {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @ManyToOne // 어떤 study를 만든 사람은 여러 개를 만들 수 있다.   // 단방향에서의 관계의 주인은 관계를 정의한 쪽이다.
    private Account owner;


}
