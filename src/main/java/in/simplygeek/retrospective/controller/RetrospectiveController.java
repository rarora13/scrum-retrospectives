package in.simplygeek.retrospective.controller;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import in.simplygeek.retrospective.beans.FeedbackBean;
import in.simplygeek.retrospective.beans.RetrospectiveBean;
import in.simplygeek.retrospective.entities.Retrospective;
import in.simplygeek.retrospective.service.RetrospectiveService;

@RestController
@RequestMapping(path = "/api/v1/retrospectives", 
produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}, 
consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
public class RetrospectiveController {
	
	private static final Logger logger = LoggerFactory.getLogger(RetrospectiveController.class);
	
    private final RetrospectiveService retrospectiveService;
    
    @Autowired
    public RetrospectiveController(RetrospectiveService retrospectiveService) {
        this.retrospectiveService = retrospectiveService;
    }
    
    
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public ResponseEntity<Retrospective> createRetrospective(@RequestBody RetrospectiveBean retrospective) {
    	logger.info("create Retrospective request recieved-> {} ",retrospective);
        Retrospective createdRetrospective = retrospectiveService.createRetrospective(retrospective);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRetrospective);
    }

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Page<Retrospective>> getRetrospective(
    		@RequestParam(name="date",required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date date,
    		@RequestParam(name="comparisonType",required = false) String comparisonType,
    		@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
    	logger.info("get Retrospective request recieved-> date:{},  comparisonType:{},  page:{}, size:{}",date, comparisonType, page, size);
    	Pageable pageable = PageRequest.of(page, size);
        Page<Retrospective> retrospectives = retrospectiveService.getRetrospectives(date, comparisonType, pageable);
        return ResponseEntity.ok().body(retrospectives);
    }
    
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/{retroId}/feedback")
    public ResponseEntity<Retrospective> createFeedback(@PathVariable Long retroId ,@RequestBody FeedbackBean feedback) {
    	logger.info("Add Feedback request recieved->  retrospectiveId:{}, feedback:{}",retroId, feedback);
        Retrospective createdRetrospective = retrospectiveService.createFeedback(retroId, feedback);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRetrospective);
    }
    
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{retroId}/feedback/{feedbackId}")
    public ResponseEntity<Retrospective> updateFeedback(@PathVariable Long retroId , @PathVariable Long feedbackId ,@RequestBody FeedbackBean feedback) {
    	logger.info("Update Feedback request recieved->  retrospectiveId:{}, feedbackId:{}, Feedback: {}",retroId, feedbackId, feedback);
    	Retrospective createdRetrospective = retrospectiveService.updateFeedback(retroId, feedbackId, feedback);
        return ResponseEntity.ok().body(createdRetrospective);
    }
}
