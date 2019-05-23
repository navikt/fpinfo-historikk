package no.nav.foreldrepenger.historikk.domain;

import javax.persistence.Entity;

@Entity
public class Customer {
    private String firstName;
    private String lastName;

    protected Customer() {
    }

    public Customer(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [firstName=" + firstName + ", lastName=" + lastName + "]";
    }
}
