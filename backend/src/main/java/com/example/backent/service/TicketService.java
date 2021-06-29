package com.example.backent.service;

import com.example.backent.entity.*;
import com.example.backent.entity.enums.TicketCondition;
import com.example.backent.payload.*;
import com.example.backent.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TicketService {

  @Autowired TicketRepository ticketRepository;

  @Autowired UserRepository userRepository;

  @Autowired BoardRepository boardRepository;

  @Autowired ProgrammingLanguageRepository programmingLanguageRepository;

  @Autowired CompleteQuestionRepository completeQuestionRepository;

  public ApiResponseModel addOrEditTicket(ReqTicket reqTicket) {
    Ticket ticket = getTicket(reqTicket.getId());
    ticket.setWorkType(reqTicket.getWorkType());
    ticket.setText(reqTicket.getText());
    ticket.setTicketCondition(TicketCondition.CREATED);
    ticket.setWorker(getUser(reqTicket.getWorkerId()));
    ticket.setPm(getUser(reqTicket.getPmId()));
    ticket.setTester(getUser(reqTicket.getTesterId()));
    ticket.setHoursWorker(reqTicket.getHoursWorker());
    ticket.setHoursTester(reqTicket.getHoursTester());
    ticket.setBoard(getBoard(reqTicket.getBoard()));
    ticket.setProgramingLanguage(getProgramingLanguage(reqTicket.getProgramingLanguage()));
    ticket.setCompleteQuestion(getCompleteQuestion(ticket.getId()));
    ticketRepository.save(ticket);
    return new ApiResponseModel();
  }

  public Ticket getTicket(Long id) {
    try {
      if (id != null) {
        return ticketRepository.getById(id);
      }
      return new Ticket();
    } catch (Exception e) {
      return new Ticket();
    }
  }

  public ProgramingLanguage getProgramingLanguage(Long id) {
    try {
      if (id != null) {
        return programmingLanguageRepository.getById(id);
      }
      return new ProgramingLanguage();
    } catch (Exception e) {
      return new ProgramingLanguage();
    }
  }

  public CompleteQuestion getCompleteQuestion(Long id) {
    try {
      if (id != null) {
        return completeQuestionRepository.getById(id);
      }
      return new CompleteQuestion();
    } catch (Exception e) {
      return new CompleteQuestion();
    }
  }

  public User getUser(Long id) {
    try {
      if (id != null) {
        return userRepository.getById(id);
      }
      return new User();
    } catch (Exception e) {
      return new User();
    }
  }

  public Board getBoard(Long id) {
    try {
      if (id != null) {
        return boardRepository.getById(id);
      }
      return new Board();
    } catch (Exception e) {
      return new Board();
    }
  }

  public ApiResponseModel deleteTicket(Long id) {
    try {
      ticketRepository.deleteById(id);
      return new ApiResponseModel(HttpStatus.OK.value(), "delete");
    } catch (Exception e) {
      return new ApiResponseModel(HttpStatus.CONTINUE.value(), "not delete");
    }
  }

  public ApiResponseModel backLock(Long projectId){
    ApiResponseModel response= new ApiResponseModel();
    try{
      List<ResTicket> backlog = ticketRepository.backlog(projectId).stream().map(this::getTicket).collect(Collectors.toList());
      response.setData(backlog);
      response.setCode(200);
      response.setMessage("success");
    }catch(Exception e){
      response.setCode(500);
      response.setMessage("error");
    }
    return response;
  }

  public ApiResponseModel filterTicket(Long projectTypeId , String type , Long tagId){
    ApiResponseModel response = new ApiResponseModel();
    try{
      List<ResTicket> all = ticketRepository.findAllByTypeAndWorkType(projectTypeId, type, tagId).stream().map(this::getTicket).collect(Collectors.toList());
      response.setCode(200);
      response.setMessage("success!");
      response.setData(all);
    }catch(Exception e){
      response.setCode(200);
      response.setMessage("success!");
    }
    return response;
  }

//  public ApiResponseModel editTicket(ReqTicket reqTicket){
//    ApiResponseModel response = new ApiResponseModel();
//    try{
//      Optional<Ticket> ticket = ticketRepository.findById(reqTicket.getId());
//    }catch(Exception e){
//      response.setCode(500);
//      response.setMessage("error");
//    }
//    return response;
//  }

  public ResTicket getTicket(Ticket ticket){
    return new ResTicket(
            ticket.getId(),
            ticket.getWorkType(),
            ticket.getText(),
            ticket.getWorker()!=null ? new ResUser(
                    ticket.getWorker().getId(),
                    ticket.getWorker().getFirstname(),
                    ticket.getWorker().getEmail(),
                    ticket.getWorker()!=null? ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/attach/").path(ticket.getWorker().getId().toString()).toUriString():null
                    ) : null,
            ticket.getWorker()!=null ? new ResUser(
                    ticket.getWorker().getId(),
                    ticket.getWorker().getFirstname(),
                    ticket.getWorker().getEmail(),
                    ticket.getWorker()!=null? ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/attach/").path(ticket.getWorker().getId().toString()).toUriString():null
            ) : null,
            ticket.getWorker()!=null ? new ResUser(
                    ticket.getWorker().getId(),
                    ticket.getWorker().getFirstname(),
                    ticket.getWorker().getEmail(),
                    ticket.getWorker()!=null? ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/attach/").path(ticket.getWorker().getId().toString()).toUriString():null
            ) : null,
            ticket.getHoursWorker(),
            ticket.getHoursTester(),
            ticket.getProgramingLanguage()!=null ? new ResLanguage(
                    ticket.getProgramingLanguage().getId(),
                    ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/attach/").path(ticket.getProgramingLanguage().getLogo().getId().toString()).toUriString(),
                    ticket.getProgramingLanguage().getName()
            ):null,
            new ResCompleteQuestion(
                    ticket.getCompleteQuestion().getId(),
                    ticket.getCompleteQuestion().getQuestionPhoto()!=null?
                            ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/attach/").path( ticket.getCompleteQuestion().getQuestionPhoto().getId().toString()).toUriString():null,
                    ticket.getCompleteQuestion().getText(),
                    ticket.getCompleteQuestion().getLink()
            ),
            ticket.getTag()
    );
  }
}
