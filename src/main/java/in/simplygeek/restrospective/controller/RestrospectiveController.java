package in.simplygeek.restrospective.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import in.simplygeek.restrospective.beans.FeedbackBean;
import in.simplygeek.restrospective.beans.RetrospectiveBean;
import in.simplygeek.restrospective.entities.Retrospective;
import in.simplygeek.restrospective.service.RestrospectiveService;

@RestController
@RequestMapping("/api/v1/retrospectives")
public class RestrospectiveController {
    private final RestrospectiveService retrospectiveService;
    
    @Autowired
    public RestrospectiveController(RestrospectiveService retrospectiveService) {
        this.retrospectiveService = retrospectiveService;
    }
    
    
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public ResponseEntity<Retrospective> createRetrospective(@RequestBody RetrospectiveBean retrospective) {
        Retrospective createdRetrospective = retrospectiveService.createRetrospective(retrospective);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRetrospective);
    }

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Page<Retrospective>> getRetrospective(
    		@RequestParam(name="date",required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date date,
    		@RequestParam(name="comparisonType",required = false) String comparisonType,
    		@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
    	Pageable pageable = PageRequest.of(page, size);
        Page<Retrospective> retrospectives = retrospectiveService.getRetrospectives(date, comparisonType, pageable);
        return ResponseEntity.ok().body(retrospectives);
    }
    
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/{retroId}/feedback")
    public ResponseEntity<Retrospective> createFeedback(@PathVariable Long retroId ,@RequestBody FeedbackBean feedback) {
        Retrospective createdRetrospective = retrospectiveService.createFeedback(retroId, feedback);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRetrospective);
    }
    
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{retroId}/feedback/{feedbackId}")
    public ResponseEntity<Retrospective> createFeedback(@PathVariable Long retroId , @PathVariable Long feedbackId ,@RequestBody FeedbackBean feedback) {
        Retrospective createdRetrospective = retrospectiveService.updateFeedback(retroId, feedbackId, feedback);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRetrospective);
    }
}
