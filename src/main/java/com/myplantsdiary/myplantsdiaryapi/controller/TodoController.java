package com.myplantsdiary.myplantsdiaryapi.controller;

import com.myplantsdiary.myplantsdiaryapi.dto.PageRequestDTO;
import com.myplantsdiary.myplantsdiaryapi.dto.PageResponseDTO;
import com.myplantsdiary.myplantsdiaryapi.dto.TodoDTO;
import com.myplantsdiary.myplantsdiaryapi.service.TodoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.web.bind.annotation.*;

@RestController
@Log4j
@RequiredArgsConstructor
@RequestMapping("/api/todo")
public class TodoController {

    private final TodoService todoService;

    @GetMapping("/{tno}")   //queryString 아닌 pathvariable 사용하는 이유 : identity 분명하다. 변경되지 않음.
    public TodoDTO getTodo(@PathVariable("tno") Long tno) {
        return todoService.get(tno);
    }

    @GetMapping("/list")    //ex)page 3인 경우 매번 변경 가능함.
    public PageResponseDTO<TodoDTO> list(PageRequestDTO pageRequestDTO){

        log.info("...list........ " + pageRequestDTO);

        return todoService.getList(pageRequestDTO);
    }


}
