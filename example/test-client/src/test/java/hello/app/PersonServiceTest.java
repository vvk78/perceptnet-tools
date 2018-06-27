package hello.app;


import hello.app.person.PersonDto;
import org.testng.annotations.Test;

import java.util.Date;
import java.util.List;

import static hello.app.RestServiceProvider.*;
import static org.testng.Assert.*;

/**
 * created by vkorovkin on 20.06.2018
 */
public class PersonServiceTest extends BaseServiceViaRestClientTest  {

    @Test
    public void testPersonCrud() {
        List<PersonDto> initialPersons = getPersonService().loadPersons();
        PersonDto pA = new PersonDto();
        pA.setFirstName("Vasia");
        pA.setLastName("Pupkin");
        pA.setDateOfBirth(new Date());

        Long pAId = getPersonService().createPerson(pA);
        assertEquals(getPersonService().loadPersons().size(), initialPersons.size() + 1, "Persons number is the same");

        PersonDto pALoaded = getPersonService().loadPerson(pAId);
        assertEquals(pALoaded.getId(), pAId);
        assertEquals(pALoaded.getFirstName(), pA.getFirstName());
        assertEquals(pALoaded.getLastName(), pA.getLastName());
        assertEquals(pALoaded.getDateOfBirth(), pA.getDateOfBirth());

        pA = pALoaded;
        pA.setFirstName("Petia");
        pA.setLastName("Mokin");
        pA.setDateOfBirth(new Date(System.currentTimeMillis() - 10000000));

        getPersonService().updatePerson(pA);
        pALoaded = getPersonService().loadPerson(pAId);
        assertEquals(pALoaded.getId(), pAId);
        assertEquals(pALoaded.getFirstName(), pA.getFirstName());
        assertEquals(pALoaded.getLastName(), pA.getLastName());
        assertEquals(pALoaded.getDateOfBirth(), pA.getDateOfBirth());

        assertEquals(getPersonService().loadPersons().size(), initialPersons.size() + 1, "Persons number is the same");

        getPersonService().deletePerson(pAId);
        assertEquals(getPersonService().loadPersons().size(), initialPersons.size(), "Persons number is the same");

        try {
            getPersonService().loadPerson(pAId);
        } catch (Exception expected) {
            System.out.println("Expected exception: " + expected);
        }
    }

    @Test(expectedExceptions = {BusinessLogicException.class})
    public void testExceptionHandling() {
        getPersonService().loadNonExistingPerson(666L);
    }

}
