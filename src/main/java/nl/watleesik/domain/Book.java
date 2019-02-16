package nl.watleesik.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    
    private long isbn;
    
    @ManyToOne
    private BookCategory bookCategory;
    
    @ManyToOne
    @JoinColumn(name = "author_id")
    private Author author;
    
    private String title;
    private int publicationYear;
    private int pages;
    
    @OneToMany
    private List<Rating> ratingList;

}
