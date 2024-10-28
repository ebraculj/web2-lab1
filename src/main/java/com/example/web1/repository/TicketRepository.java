package com.example.web1.repository;

import com.example.web1.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TicketRepository extends JpaRepository<Ticket, UUID> {
    List<Ticket> findByVatin(String vatin); //provjera koja služi za uvjet max 3 ulaznice po kupcu



    long count();
}
