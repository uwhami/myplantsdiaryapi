package com.myplantsdiary.myplantsdiaryapi.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@ToString
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tbl_todo")
@Setter
public class Todo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tno;

    @Column(length=500, nullable = false)
    private String title;

    private String content;

    private boolean complete;

    private LocalDate dueDate;

    private String writer;


}
