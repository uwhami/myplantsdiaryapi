package com.myplantsdiary.myplantsdiaryapi.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Data
public class PageResponseDTO<E> {

    private List<E> dataList;

    private List<Integer> pageNumList;

    private PageRequestDTO pageRequestDTO;

    private boolean prev, next;

    private int totalCount, prevPage, nextPage, totalPage, currentPage;

    @Builder(builderMethodName = "withAll")
    public PageResponseDTO(List<E> dtoList, PageRequestDTO pageRequestDTO, long total) {

        this.dataList = dtoList;
        this.pageRequestDTO = pageRequestDTO;
        this.totalCount = (int) total;

        //끝페이지 계산.
        this.currentPage = pageRequestDTO.getPage();
        int curSize = pageRequestDTO.getSize();
        int startPage = (currentPage-1)/10 * 10 + 1;
        int endPage = Math.min(startPage + 9, ((totalCount-1)/curSize)+1);

        this.prev = startPage > 1;
        this.next = (totalCount/curSize) > endPage;

        this.pageNumList = IntStream.rangeClosed(startPage, endPage).boxed().collect(Collectors.toList());

        this.prevPage = prev ? startPage - 1 : 0;
        this.nextPage = next ? endPage + 1 : 0;


    }

}
