package demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class MainController {

    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private TrackRepository trackRepository;

    @GetMapping(path="/persons", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Iterable<Person>> getPersons() {
        Collection<Person> persons = (Collection<Person>) personRepository.findAll();
        if (!persons.isEmpty()) {
            return ResponseEntity.ok(persons);
        } else {
            return ResponseEntity.noContent().build();
        }
    }
    @GetMapping(value = "/persons/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Person> getPerson(@PathVariable("id") long id) {
        Optional<Person> person = personRepository.findById(id);
        if (person.isPresent()) {
            return ResponseEntity.ok(person.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(path="/persons", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Void> addPerson(String firstName, String lastName) {
        Person person = new Person(firstName, lastName);
        personRepository.save(person);
        URI uri = WebMvcLinkBuilder.linkTo(MainController.class).slash("persons").slash(person.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }
    @PutMapping(path="/persons/{id}")
    public ResponseEntity<Void> changePerson(@PathVariable("id") long id, @RequestBody Person entity) {
        Optional<Person> existingPerson = personRepository.findById(id);
        if(existingPerson.isPresent()) {
            existingPerson.get().update(entity);
            personRepository.save(existingPerson.get());
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping(path = "/persons/{id}")
    public ResponseEntity<Void> removePerson(@PathVariable("id") long id) {
        Optional<Person> existingPerson = personRepository.findById(id);
        if (existingPerson.isPresent()) {
            for (Track track : existingPerson.get().getTracks()) {
                track.getAttendees().remove(existingPerson.get());
                trackRepository.save(track);
            }
            personRepository.delete(existingPerson.get());
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(path="/persons/{personId}/tracks")
    public ResponseEntity<Set<Track>> getPersonTracks(@PathVariable("personId") long personId) {
        Optional<Person> person = personRepository.findById(personId);
        if (person.isPresent()) {
            Set<Track> tracks = person.get().getTracks();
            if(!tracks.isEmpty()) {
                return ResponseEntity.ok(tracks);
            } else {
                return ResponseEntity.noContent().build();
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(path="/persons/{personId}/tracks/{trackId}")
    public ResponseEntity<Void> addPersonTrack(@PathVariable("personId") long personId, @PathVariable("trackId") long trackId) {
        Optional<Person> person = personRepository.findById(personId);
        if (person.isPresent()) {
            Optional<Track> track = trackRepository.findById(trackId);
            if(track.isPresent() && track.get().getSpeaker().getId() != personId) {
                person.get().addTrack(track.get());
                personRepository.save(person.get());
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.badRequest().build();
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping(path="/persons/{personId}/tracks/{trackId}")
    public ResponseEntity<Void> removePersonTrack(@PathVariable("personId") long personId, @PathVariable("trackId") long trackId) {
        Optional<Person> person = personRepository.findById(personId);
        if (person.isPresent()) {
            Optional<Track> track = trackRepository.findById(trackId);
            if(track.isPresent()) {
                person.get().removeTrack(track.get());
                personRepository.save(person.get());
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.badRequest().build();
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(path="/tracks", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Iterable<Track>> getTracks() {
        Collection<Track> tracks = (Collection<Track>) trackRepository.findAll();
        if (!tracks.isEmpty()) {
            return ResponseEntity.ok(tracks);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping(path = "/tracks/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Track> getTrack(@PathVariable("id") long id) {
        Optional<Track> track = trackRepository.findById(id);
        if(track.isPresent()) {
            return ResponseEntity.ok(track.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(path="/tracks", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Void> addTrack (String title, String description, long speakerId) {
        Optional<Person> speaker = personRepository.findById(speakerId);
        if(speaker.isPresent()) {
            Track track = new Track(title, description, speaker.get());
            trackRepository.save(track);
            URI uri = WebMvcLinkBuilder.linkTo(MainController.class).slash("tracks").slash(track.getId()).toUri();
            return ResponseEntity.created(uri).build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping(path = "/tracks/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> changeTrack(@PathVariable("id") long id, @RequestBody Track track) {
        Optional<Track> existingTrack = trackRepository.findById(id);
        if(existingTrack.isPresent()) {
            existingTrack.get().update(track);
            trackRepository.save(existingTrack.get());
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping(path = "/tracks/{id}")
    public ResponseEntity<Void> deleteTrack(@PathVariable("id") long id) {
        Optional<Track> existingTrack = trackRepository.findById(id);
        if(existingTrack.isPresent()) {
            for (Person person : existingTrack.get().getAttendees()) {
                person.removeTrack(existingTrack.get());
                personRepository.save(person);
            }
            trackRepository.delete(existingTrack.get());
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(path="/tracks/{id}/attendees")
    public ResponseEntity<Set<Person>> getTrackAttendees(@PathVariable("id") long id) {
        Optional<Track> existingTrack = trackRepository.findById(id);
        if(existingTrack.isPresent()) {
            Set<Person> attendees = existingTrack.get().getAttendees();
            if (!attendees.isEmpty()) {
                return ResponseEntity.ok(attendees);
            } else {
                return ResponseEntity.noContent().build();
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
