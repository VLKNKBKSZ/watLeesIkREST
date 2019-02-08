package nl.watleesik.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @OneToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;
    @OneToMany
    @JoinColumn(name = "address_id")
    private List<Address> addressList;
    private LocalDateTime dayOfBirth;
    private String name;
    private String middleName;
    private String lastName;
    private LocalDateTime updatedAt;
    @ManyToMany
    @JoinTable(name = "person_book", joinColumns = @JoinColumn(name =
            "person_id"), inverseJoinColumns = @JoinColumn(name = "book_id"))
    private List<Book> bookList;


}