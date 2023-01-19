package by.matthewvirus.sweater.repository;

import by.matthewvirus.sweater.entity.Message;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MessageRepository extends CrudRepository<Message, Integer> {

    List<Message> findByTag(String tag);
}