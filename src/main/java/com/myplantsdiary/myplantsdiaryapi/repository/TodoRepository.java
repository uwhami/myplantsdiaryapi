package com.myplantsdiary.myplantsdiaryapi.repository;

import com.myplantsdiary.myplantsdiaryapi.domain.Todo;
import com.myplantsdiary.myplantsdiaryapi.repository.search.TodoSearch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoRepository extends JpaRepository<Todo, Long> , TodoSearch {



}
