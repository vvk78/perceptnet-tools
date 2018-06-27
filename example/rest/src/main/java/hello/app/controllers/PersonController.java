package hello.app.controllers;

import hello.app.person.PersonDto;
import hello.app.person.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * created by vkorovkin on 20.06.2018
 */
@RestController
@RequestMapping("person")
public class PersonController {
    private PersonService personService;

    @GetMapping("/all")
    public List<PersonDto> loadPersons() {
        return personService.loadPersons();
    }

    @GetMapping("/{personId}")
    public PersonDto loadPerson(@PathVariable("personId") Long personId) {
        return personService.loadPerson(personId);
    }

    @GetMapping("/nonexisting/{personId}")
    public PersonDto loadNonExistingPerson(@PathVariable("personId") Long personId) {
        return personService.loadNonExistingPerson(personId);
    }

    @PostMapping
    public Long createPerson(@RequestBody PersonDto person) {
        return personService.createPerson(person);
    }

    @PutMapping
    public void updatePerson(@RequestBody PersonDto person) {
        personService.updatePerson(person);
    }

    @DeleteMapping("/{personId}")
    public void deletePerson(@PathVariable("personId") Long personId) {
        personService.deletePerson(personId);
    }

    @Autowired
    public void setPersonService(PersonService personService) {
        this.personService = personService;
    }
}
