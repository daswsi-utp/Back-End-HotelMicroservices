package com.microservice_rooms.service;

import com.microservice_rooms.entities.Tag;
import com.microservice_rooms.persistence.TagRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ImplServiceTag implements IServiceTag{

    private final TagRepository tagRepository;

    public  ImplServiceTag(TagRepository tagRepository){
        this.tagRepository= tagRepository;
    }
    @Override
    public Tag createTag(String name) {
        return tagRepository.findByName(name).orElseGet(() -> {
            Tag tag = new Tag();
            tag.setName(name);
            return tagRepository.save(tag);
        });
    }

    @Override
    public Optional<Tag> getTagById(Long id) {
        return tagRepository.findById(id);
    }

    @Override
    public Optional<Tag> getTagByName(String name) {
        return tagRepository.findByName(name);
    }

    @Override
    public List<Tag> getAllTags() {
        return tagRepository.findAll();
    }

    @Override
    public void deleteTag(Long id) {
        tagRepository.deleteById(id);
    }
}
