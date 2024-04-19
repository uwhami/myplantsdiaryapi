package com.myplantsdiary.myplantsdiaryapi.repository.search;

import com.myplantsdiary.myplantsdiaryapi.domain.Todo;
import com.myplantsdiary.myplantsdiaryapi.dto.PageRequestDTO;
import org.springframework.data.domain.Page;

public interface TodoSearch {
    Page<Todo> search1(PageRequestDTO pageRequestDTO);
}
