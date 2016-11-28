package io.github.fbiville.trainings.neo4j._0_basics;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

/**
 * This class is just a warm-up ;-)
 */
public class BasicsTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void should_perform_addition() {
        int five = 2 + 3;

        // please do not change the assertion, it describes the desired result is!
        assertThat(five).isEqualTo(5);
    }

    @Test
    public void should_contain_all_specified_numbers() {
        List<Integer> ints = Arrays.asList(1,2,3);

        // please do not change the assertion, it describes the desired result is!
        assertThat(ints).containsExactly(1, 2, 3);
    }

    @Test
    public void should_extract_all_first_names() {
        List<Person> persons = Arrays.asList(
            new Person("Peter", "Neubauer"),
            new Person("Emil", "Eifrem"));

        // please do not change the assertion, it describes the desired result is!
        assertThat(persons)
            .extracting(Person::getFirstName)
            .containsExactly("Peter", "Emil");
    }

    @Test
    public void should_fail_dividing_by_zero() {
        thrown.expect(ArithmeticException.class);

        int denominator = 0;
        double ignored = 1 / denominator;
        // do not remove this fail call
        fail("You should change the denominator to trigger the expected failure");
    }

    private static class Person {
        private final String firstName;
        private final String lastName;

        public Person(String firstName, String lastName) {
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
        public int hashCode() {
            return Objects.hash(firstName, lastName);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            final Person other = (Person) obj;
            return Objects.equals(this.firstName, other.firstName)
                && Objects.equals(this.lastName, other.lastName);
        }
    }

}
