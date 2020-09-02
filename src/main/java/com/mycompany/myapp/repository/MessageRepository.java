package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Message;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the Message entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query("select message from Message message where message.sender.login = ?#{principal.username}")
    List<Message> findBySenderIsCurrentUser();

    @Query("select message from Message message where message.receiver.login = ?#{principal.username}")
    List<Message> findByReceiverIsCurrentUser();


    @Query("select message from Message message where message.receiver.login = ?#{principal.username} OR message.sender.login = ?#{principal.username} ")
    List<Message> findAllByUserIsCurrentUser();

 
}
