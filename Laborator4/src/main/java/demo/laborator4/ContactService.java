package demo.laborator4;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collection;

@Service
public class ContactService {
    @Autowired
    private ContactRepository contactRepository;

    public Collection<ContactModel> getAllContacts() {
        return contactRepository.findAll();
    }
    public ContactModel getContactById(String id) {
        return contactRepository.findById(id);
    }
    public void removeContact(String id) throws IOException {
        contactRepository.deleteById(id);
    }
    public void saveContact(ContactModel contactModel) throws IOException {
        contactRepository.save(contactModel);
    }
}
