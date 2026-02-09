package ru.oldzoomer.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.proxy.HibernateProxy;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "clients")
@Getter
@Setter
@ToString
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String surname;

    private String middleName;

    private String vinNumber;

    private String phone;

    private String email;

    @OneToMany(mappedBy = "client", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @ToString.Exclude
    private List<Order> orders;

    @Override
    public final boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        Class<?> objectEffectiveClass = o instanceof HibernateProxy proxy ? proxy.getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy proxy ? proxy.getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != objectEffectiveClass) {
            return false;
        }
        Client client = (Client) o;
        return getId() != null && Objects.equals(getId(), client.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy proxy ? proxy.getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
