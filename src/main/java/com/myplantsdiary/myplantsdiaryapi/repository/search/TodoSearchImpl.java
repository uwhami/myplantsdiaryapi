package com.myplantsdiary.myplantsdiaryapi.repository.search;

import com.myplantsdiary.myplantsdiaryapi.domain.QTodo;
import com.myplantsdiary.myplantsdiaryapi.domain.Todo;
import com.myplantsdiary.myplantsdiaryapi.dto.PageRequestDTO;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;
import java.util.Objects;

@Log4j2
public class TodoSearchImpl extends QuerydslRepositorySupport implements TodoSearch{

    public TodoSearchImpl() {
        super(Todo.class);
    }

    @Override
    public Page<Todo> search1(PageRequestDTO pageRequestDTO) {
//        log.info("search1................");
        QTodo todo = QTodo.todo;
        JPQLQuery<Todo> query = from(todo);
//        query.where(todo.title.contains("1"));

        Pageable pageable = PageRequest.of(
                pageRequestDTO.getPage()-1,
                pageRequestDTO.getSize(),
                Sort.by("tno").descending());

        Objects.requireNonNull(this.getQuerydsl()).applyPagination(pageable, query);

        List<Todo> list = query.fetch();
        long total = query.fetchCount();

        return new PageImpl<>(list, pageable, total);
    }
}
