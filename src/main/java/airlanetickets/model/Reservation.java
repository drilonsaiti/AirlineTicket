package airlanetickets.model;


import airlanetickets.model.enumerations.ClassesType;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String name;

    String surname;

    String numberOfPassport;

    String numberOfPhone;

    @Enumerated(value = EnumType.STRING)
    ClassesType classesType;


    public Reservation(String name, String surname, String numberOfPassport,String numberOfPhone,ClassesType classesType) {
        this.name = name;
        this.surname = surname;
        this.numberOfPassport = numberOfPassport;
        this.numberOfPhone = numberOfPhone;
        this.classesType = classesType;
    }

    public Reservation() {
    }

}
