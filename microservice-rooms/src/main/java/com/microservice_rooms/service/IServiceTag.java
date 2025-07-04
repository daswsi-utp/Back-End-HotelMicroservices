package com.microservice_rooms.service;
import com.microservice_rooms.entities.Tag;

import java.util.List;
import java.util.Optional;
public interface IServiceTag {
    Tag createTag(String name);
    Optional<Tag> getTagById(Long id);
    Optional<Tag> getTagByName(String name);
    List<Tag> getAllTags();
    void deleteTag(Long id);
}
