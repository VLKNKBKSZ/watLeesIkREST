package nl.watleesik.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    
//    @OneToMany
//    @JoinColumn(name = "address_id")
//    private List<Address> addressList;
    @OneToOne(cascade=CascadeType.ALL)
    Address address;
    
    private LocalDate dayOfBirth;
    private String name;
    private String middleName;
    private String lastName;
    private LocalDateTime updatedOn;
    
    @ManyToMany
    @JoinTable(name = "person_book", joinColumns = @JoinColumn(name =
            "person_id"), inverseJoinColumns = @JoinColumn(name = "book_id"))
    private List<Book> bookList;


}
