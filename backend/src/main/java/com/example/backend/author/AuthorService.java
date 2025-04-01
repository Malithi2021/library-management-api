package com.example.backend.author;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class AuthorService {
    private final AuthorRepository authorRepository;

    @Autowired
    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public List<Author> getAllAuthors(){
        return  authorRepository.findAll();
    }

    public Author getAuthorById(Long id){
        return authorRepository.findById(id).orElseThrow(() -> new IllegalStateException("Author with id" + id + "does not exist"));
    }

    public void addAuthor(Author author){
        authorRepository.save(author);
    }

    @Transactional
    public void updateAuthor(Long authorId, Author authorDetails){
        Author author = authorRepository.findById(authorId).orElseThrow(() -> new IllegalStateException("author with id" + authorId + "does not exist"));

        if (authorDetails.getName() != null && authorDetails.getName().length()>0 &&
        !Objects.equals(author.getName(),authorDetails.getName())){
            author.setName(authorDetails.getName());
        }

        if (authorDetails.getBiography() != null && authorDetails.getBiography().length()>0 &&
                !Objects.equals(author.getBiography(),authorDetails.getBiography())){
            author.setBiography(authorDetails.getBiography());
        }
    }

    public void deleteAuthor(Long authorId) {
        boolean exists = authorRepository.existsById(authorId);
        if (!exists) {
            throw new IllegalStateException(
                    "Author with id " + authorId + " does not exist");
        }
        authorRepository.deleteById(authorId);
    }

    public List<Author> searchAuthorsByName(String name) {
        return authorRepository.findByNameContainingIgnoreCase(name);
    }
}
