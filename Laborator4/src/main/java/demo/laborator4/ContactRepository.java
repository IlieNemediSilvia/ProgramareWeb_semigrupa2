package demo.laborator4;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class ContactRepository {

    private File file;
    private Map<String, ContactModel> contacts;

    public ContactRepository (@Value("${repository}") String repository) throws IOException {
        file = Paths.get(repository).toFile();
        contacts = file.exists() ?
                Arrays.stream(new ObjectMapper().readValue(file, ContactModel[].class))
                        .collect(Collectors.toMap(contact -> contact.getId(), contact -> contact))
                : new HashMap<>();
    }
    public Collection<ContactModel> findAll() {
        return contacts.values();
    }

    public ContactModel findById(String id) {
        if(contacts.containsKey(id)) {
            return contacts.get(id);
        } else {
            throw new IllegalArgumentException("Invalid contact id " + id);
        }
    }
    private void writeToFile() throws IOException {
        new ObjectMapper().writeValue(file, findAll());
    }
    public void deleteById(String id) throws IOException {
        if(contacts.containsKey(id)) {
            contacts.remove(id);
            writeToFile();
        } else {
            throw new IllegalArgumentException("Invalid contact id " + id);
        }
    }
    public void save(ContactModel contactModel) throws IOException {
        if(contacts.containsKey(contactModel.getId())) {
            contacts.get(contactModel.getId()).setFirstName(contactModel.getFirstName());
            contacts.get(contactModel.getId()).setLastName(contactModel.getLastName());
            contacts.get(contactModel.getId()).setEmail(contactModel.getEmail());
        } else {
            contacts.put(contactModel.getId(), contactModel);
        }
        writeToFile();
    }
}
