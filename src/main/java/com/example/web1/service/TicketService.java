package com.example.web1.service;

import com.example.web1.repository.TicketRepository;

import java.awt.image.BufferedImage;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import com.example.web1.model.Ticket;
import com.google.zxing.WriterException;
import org.springframework.stereotype.Service;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;

    public TicketService(TicketRepository ticketRepository){
        this.ticketRepository = ticketRepository;
    }

    public Optional<Ticket> getTicketByUuid(UUID uuid) { return ticketRepository.findById(uuid); }

    public Ticket createTicket(String vatin, String firstName, String lastName){
        List<Ticket> existingTickets = ticketRepository.findByVatin(vatin);
        if(existingTickets.size() >= 3){

        }
        Ticket t = new Ticket(vatin, firstName, lastName, LocalDateTime.now());
        return ticketRepository.save(t);
    }

    public List<Ticket> getTicketsByVatin(String vatin){

        return ticketRepository.findByVatin(vatin);
    }

    public long numOfTickets(){

        return ticketRepository.count();
    }

}
