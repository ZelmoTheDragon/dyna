package com.github.zelmothedragon.dyna.search;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import javax.json.bind.annotation.JsonbProperty;
import javax.json.bind.annotation.JsonbPropertyOrder;
import javax.json.bind.config.PropertyOrderStrategy;

@JsonbPropertyOrder(PropertyOrderStrategy.LEXICOGRAPHICAL)
public class Pagination implements Serializable {

    public static final int DEFAULT_PAGE_SIZE = 100;

    public static final int DEFAULT_PAGE_NUMBER = 1;

    @JsonbProperty("entity")
    private String entity;

    @JsonbProperty("pageSize")
    private Integer pageSize;

    @JsonbProperty("pageNumber")
    private Integer pageNumber;

    @JsonbProperty("pageCount")
    private Integer pageCount;

    @JsonbProperty("count")
    private Integer count;

    @JsonbProperty("data")
    private List<Object> data;

    public Pagination() {
        this.pageSize = DEFAULT_PAGE_SIZE;
        this.pageNumber = DEFAULT_PAGE_NUMBER;
        this.pageCount = 0;
        this.count = 0;
        this.data = List.of();
    }

    @Override
    public int hashCode() {
        return Objects.hash(entity, pageSize, pageNumber, pageCount, count, data);
    }

    @Override
    public boolean equals(Object obj) {
        final boolean eq;
        if (this == obj) {
            eq = true;
        } else if (!(obj instanceof Pagination)) {
            eq = false;
        } else {
            var other = (Pagination) obj;
            eq = Objects.equals(entity, other.entity)
                    && Objects.equals(pageSize, other.pageSize)
                    && Objects.equals(pageNumber, other.pageNumber)
                    && Objects.equals(pageCount, other.pageCount)
                    && Objects.equals(count, other.count)
                    && Objects.equals(data, other.data);
        }
        return eq;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = Math.max(pageSize, DEFAULT_PAGE_SIZE);
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = Math.max(pageNumber, DEFAULT_PAGE_NUMBER);
    }

    public Integer getPageCount() {
        return pageCount;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
        this.pageCount = count / pageSize;
    }

    public List<Object> getData() {
        return data;
    }

    public void setData(List<Object> data) {
        this.data = data;
    }

}
