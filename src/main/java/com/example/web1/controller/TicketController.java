package com.example.web1.controller;

import com.example.web1.model.Ticket;
import com.example.web1.repository.TicketRepository;
import com.example.web1.service.TicketService;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.Writer;
import java.net.URI;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ticket")
@CrossOrigin(origins = "*")
public class TicketController {
    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private TicketService ticketService;

    public TicketController(TicketRepository ticketRepository){
        this.ticketRepository = ticketRepository;
    }
    @PostMapping("/create")
    public ResponseEntity<?> createTicket(@RequestBody Map<String, Object> ticketData){
        String vatin = (String) ticketData.get("vatin");
        String firstName = (String) ticketData.get("firstName");
        String lastName = (String) ticketData.get("lastName");

        if(vatin == null || firstName == null ||lastName == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Svaki kupac mora imati sljedeće podatke: ime, prezime i OIB.");
        }
        List<Ticket> existingTickets = ticketRepository.findByVatin(vatin);
        if(existingTickets.size() >= 3){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Svaki kupac može kupiti najviše 3 karte.");
        }
        Ticket newTicket = new Ticket();
        newTicket.setVatin(vatin);
        newTicket.setFirstName(firstName);
        newTicket.setLastName(lastName);
        newTicket.setDateTime(LocalDateTime.now());

        Ticket saved = ticketRepository.save(newTicket);

        try{
            //String qrCodeurl = "http://localhost:8080/ticket.html?id=" + saved.getUuid();
            String qrCodeurl = "http://localhost:8080/api/ticket/ticket-data/" + saved.getUuid();
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bm = qrCodeWriter.encode(qrCodeurl, BarcodeFormat.QR_CODE, 200, 200);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bm, "PNG", baos);
            byte[] qrCodeImage = baos.toByteArray();

            return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.IMAGE_PNG).body(qrCodeImage);
        }catch (WriterException | IOException e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Greška pri generiranj QR koda: " + e.getMessage());

        }
    }
    @GetMapping("/count")
    public ResponseEntity<Long> numOfTickets(){
        Long count = ticketRepository.count();
        System.out.println(count);
        return ResponseEntity.ok(count);
    }

    @GetMapping(value = "/ticket-data/{uuid}")//, produces = "application/json")
    public ResponseEntity<?> getTicketById(@PathVariable UUID uuid) {
        String redirectUrl = "/ticket.html?ticketId=" + uuid;
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(redirectUrl));
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
       /* Optional<Ticket> ticket = ticketRepository.findById(uuid);

        if (ticket.isPresent()) {
            // Ako ulaznica postoji, vrati ju s statusom 200 OK
            return ResponseEntity.ok(ticket.get());
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Ne postoji ulaznica s UUID: " + uuid);*/
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTicketData(@PathVariable UUID id){
        //System.out.println(id.getClass());
        Optional<Ticket> ticket = ticketService.getTicketByUuid(id);
        //System.out.println(ticket.get().getVatin());
        if (ticket.isPresent()) {
            // Ako ulaznica postoji, vrati ju s statusom 200 OK
            //return ticket.map(ResponseEntity::ok)
            //.orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
            return ResponseEntity.ok(ticket.get());
            //}
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ne postoji ulaznica s UUID: " + id);
    }


       // return ResponseEntity.status(HttpStatus.NOT_FOUND)
         //       .body("Ne postoji ulaznica s UUID: " + id);




    }

