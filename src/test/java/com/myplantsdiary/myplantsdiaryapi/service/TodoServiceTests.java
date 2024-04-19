package com.myplantsdiary.myplantsdiaryapi.service;

import com.myplantsdiary.myplantsdiaryapi.dto.PageRequestDTO;
import com.myplantsdiary.myplantsdiaryapi.dto.TodoDTO;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

@SpringBootTest
@Log4j2
public class TodoServiceTests {

    @Autowired
    TodoService todoService;

    @Test
    public void testGet(){
        Long tno = 1L;
        log.info(todoService.get(tno));

    }

    @Test
    public void testRegister(){
        TodoDTO todoDto = TodoDTO.builder()
                .title("TITLE 0417")
                .content("Content.. 0417")
                .dueDate(LocalDate.of(2024,4,17))
                .build();
        todoService.register(todoDto);
    }


    @Test
    public void testGetList(){
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder().page(2).size(1).build();

        log.info(todoService.getList(pageRequestDTO));
    }

}
