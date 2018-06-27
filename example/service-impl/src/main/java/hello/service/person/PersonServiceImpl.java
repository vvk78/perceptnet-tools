package hello.service.person;

import hello.app.BusinessLogicException;
import hello.app.person.PersonDto;
import hello.app.person.PersonService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * created by vkorovkin on 20.06.2018
 */
@Service
public class PersonServiceImpl implements PersonService {
    private final AtomicLong idSeq = new AtomicLong();
    private final ConcurrentHashMap<Long, PersonDto> personRepository = new ConcurrentHashMap<>();

    @Override
    public List<PersonDto> loadPersons() {
        return new ArrayList<>(personRepository.values());
    }

    @Override
    public PersonDto loadPerson(Long personId) {
        PersonDto result = personRepository.get(personId);
        if (result == null) {
            throw new NoSuchElementException("Person with id " + personId + " is not found");
        }
        return result;
    }

    @Override
    public Long createPerson(PersonDto person) {
        for (int i = 0; i < 100; i++) {
            Long id = idSeq.addAndGet(1);
            person.setId(id);
            if (personRepository.put(id, person) == null) {
                return id;
            }
        }
        throw new IllegalStateException("Cannot create person due to some weird racing conditions");
    }

    @Override
    public void updatePerson(PersonDto person) {
        if (person.getId() == null || !personRepository.containsKey(person.getId())) {
            throw new IllegalStateException("Person not persisted");
        }
        personRepository.put(person.getId(), person);
    }

    @Override
    public void deletePerson(Long personId) {
        personRepository.remove(personId);
    }

    @Override
    public PersonDto loadNonExistingPerson(Long personId) {
        throw new BusinessLogicException("Person with id " + personId + " does not exist");
    }
}
