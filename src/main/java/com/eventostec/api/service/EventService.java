package com.eventostec.api.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.eventostec.api.domain.event.Event;
import com.eventostec.api.domain.event.EventRequestDTO;
import com.eventostec.api.domain.event.EventResponseDTO;
import com.eventostec.api.repositories.EventRepository;

@Service
public class EventService {

    @Value("${aws.bucket.name}")
    private String bucketName;

    @Autowired
    private AmazonS3 s3Client;

    @Autowired
    private EventRepository eventRepository;



    public Event createEvent(EventRequestDTO data) {
        String imgUrl = null;

        if(data.image() != null) {
            imgUrl = this.uploadImg(data.image());
        }

        Event newEvent = new Event();
        newEvent.setTitle(data.title());
        newEvent.setDescription(data.description());
        newEvent.setEvent_url(data.eventUrl());
        newEvent.setDate(new Date(data.date()));
        newEvent.setImg_url(imgUrl);
        newEvent.setRemote(data.remote());

        eventRepository.save(newEvent);
        return newEvent;
    }

    private String uploadImg(MultipartFile multipartFile) {
        String imgName = UUID.randomUUID() + "-" + multipartFile.getOriginalFilename();

        try{
            File file = this.convertMultipartToFile(multipartFile);
            s3Client.putObject(bucketName, imgName, file);
            file.delete();
            return s3Client.getUrl(bucketName, imgName).toString();
        } catch (Exception e){
            System.out.println("erro ao subir arquivo");
            return "";
        }
    }

    private File convertMultipartToFile(MultipartFile multipartFile) throws IOException {

        File convFile = new File(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(multipartFile.getBytes());
        fos.close();
        return convFile;
    }

   public List<EventResponseDTO> getEventsUpComing(int page, int size) {
    Pageable pageable = PageRequest.of(page, size);
    Page<Event> getEventsUpComing = this.eventRepository.findUpComingEvents(new  Date(), pageable);
    return getEventsUpComing.map(event -> new EventResponseDTO(
            event.getId(),
            event.getTitle(),
            event.getDescription(),
            new java.sql.Date (event.getDate().getTime()),
            event.getRemote(),
            event.getImg_url(),
            event.getEvent_url()
    )).stream().toList();
}

}
