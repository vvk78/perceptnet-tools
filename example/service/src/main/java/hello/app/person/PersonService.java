package hello.app.person;

import hello.app.BusinessLogicException;

import java.util.List;

/**
 * created by vkorovkin on 20.06.2018
 */
public interface PersonService {

    List<PersonDto> loadPersons();

    PersonDto loadPerson(Long personId);

    Long createPerson(PersonDto person);

    void updatePerson(PersonDto person);

    void deletePerson(Long personId);

    PersonDto loadNonExistingPerson(Long personId);
}
