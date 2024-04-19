package com.myplantsdiary.myplantsdiaryapi.repository;

import com.myplantsdiary.myplantsdiaryapi.domain.Todo;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.Optional;

import static org.antlr.v4.runtime.tree.xpath.XPath.findAll;

@SpringBootTest
@Log4j2
public class TodoRepositoryTests {

    @Autowired
    private TodoRepository todoRepository;

    @Test
    public void test1(){
        Assertions.assertNotNull(todoRepository);
        log.info(todoRepository.getClass().getName());
    }

    @Test
    public void testInsert(){
        Todo todo = Todo.builder()
                .title("Title")
                .content("Content...")
                .dueDate(LocalDate.of(2023,12,22))
                .build();

        Todo result = todoRepository.save(todo);
        Assertions.assertNotNull(result);
        log.info(result);
    }

    @Test
    public void testRead(){
        Long tno = 1L;
        Optional<Todo> result = todoRepository.findById(tno);
        Todo todo = result.orElseThrow();
        log.info(todo);
    }

    @Test
    public void testUpdate(){
        Long tno = 1L;
        Optional<Todo> result = todoRepository.findById(tno);
        Todo todo = result.orElseThrow();
        log.info(todo);

        todo.setTitle("Update Title");
        todo.setContent("updated content");
        todo.setComplete(true);

        todoRepository.save(todo);

        result = todoRepository.findById(tno);
        todo = result.orElseThrow();
        log.info(todo);
    }

    @Test
    public void testPaging(){
        //페이지 번호는 0부터
        Pageable pageable = PageRequest.of(0,10, Sort.by("tno").descending());

        Page<Todo> result = todoRepository.findAll(pageable);

        log.info(result.getTotalElements());

        log.info(result.getTotalPages());

        log.info(result.getContent());

    }

//    @Test
//    public void testSearch1(){
//        todoRepository.search1();
//    }

}
