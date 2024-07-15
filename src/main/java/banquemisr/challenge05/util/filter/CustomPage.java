package banquemisr.challenge05.util.filter;

import banquemisr.challenge05.util.View;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties({"last", "first", "sort"})
@Getter
@Setter
public class CustomPage<T> extends PageImpl<T> implements  Serializable {
    private long totalElements;
    private int totalPages;
    private int size;
    private int number;
    private int numberOfElements;


    public CustomPage() {
        super(new ArrayList<>());
    }

    public CustomPage(List<T> content) {
        super(content);
    }

    public CustomPage(List<T> content, Pageable pageable, long total) {
        super(content, pageable, total);
    }

    public static <T> CustomPage<T> of(Page<T> page, Pageable pageable) {
        return new CustomPage<>(page.getContent(), pageable, page.getTotalElements());
    }

    @Override
    @JsonView(View.Page.class)
    public List<T> getContent() {
        return new ArrayList<>(super.getContent());
    }

    @Override
    @JsonView(View.Page.class)
    public int getTotalPages() {
        return Math.max(super.getTotalPages(), totalPages);
    }

    @Override
    @JsonView(View.Page.class)
    public long getTotalElements() {
        return Math.max(super.getTotalElements(), totalElements);
    }

    @Override
    @JsonView(View.Page.class)
    public int getNumber() {
        return Math.max(super.getNumber(), number);
    }

    @Override
    @JsonView(View.Page.class)
    public int getSize() {
        return Math.max(super.getSize(), size);
    }

    @JsonView(View.Page.class)
    public long getTotal() {
        return Math.max(super.getTotalElements(), totalElements);
    }

    @JsonView(View.Page.class)
    public int getNumberOfElements() {
        return Math.max(super.getNumberOfElements(), numberOfElements);
    }

    @Override
    @JsonIgnore
    public boolean isEmpty() {
        return super.isEmpty();
    }
}
