package in.simplygeek.scrumretrospective.service;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import ch.qos.logback.core.testUtil.RandomUtil;
import in.simplygeek.retrospective.beans.FeedbackBean;
import in.simplygeek.retrospective.beans.RetrospectiveBean;
import in.simplygeek.retrospective.entities.Feedback;
import in.simplygeek.retrospective.entities.Retrospective;
import in.simplygeek.retrospective.exceptions.IncorrectRequestException;
import in.simplygeek.retrospective.repository.RetrospectiveRepository;
import in.simplygeek.retrospective.service.impl.DefaultRestrospectiveService;
import jakarta.persistence.EntityNotFoundException;


public class RetrospectiveServiceTest {

    @Mock
    private RetrospectiveRepository repository;

    @InjectMocks
    private DefaultRestrospectiveService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        Mockito.reset(repository);
    }
    
    @Test
    void testCreateRetrospectiveWithValidData() {
        RetrospectiveBean retrospectiveBean = new RetrospectiveBean();
        retrospectiveBean.setName("Retrospective Name");
        retrospectiveBean.setSummary("Retrospective Summary");
        retrospectiveBean.setDate(new Date());
        retrospectiveBean.setParticipants(Set.of("Person 1", "Person 2"));

        when(repository.save(any(Retrospective.class))).thenAnswer(invocation -> {
            final Retrospective entity = invocation.getArgument(0);
            if(entity.getId() == null)
            	entity.setId((long)RandomUtil.getPositiveInt());
            return entity;
        });
        
        Retrospective createdRetrospective = service.createRetrospective(retrospectiveBean);
        
        verify(repository).save(any(Retrospective.class));
        
        assertNotNull(createdRetrospective);
        assertEquals("Retrospective Name", createdRetrospective.getName());
        assertEquals("Retrospective Summary", createdRetrospective.getSummary());
        assertEquals(retrospectiveBean.getDate(), createdRetrospective.getDate());
        assertEquals(2, createdRetrospective.getParticipants().size());
    }

    @Test
    void testCreateRetrospectiveWithIncompleteData() {
        
        RetrospectiveBean retrospectiveBean = new RetrospectiveBean();
        retrospectiveBean.setName("Retrospective");
        retrospectiveBean.setParticipants(Collections.emptySet());

        
        assertThrows(IncorrectRequestException.class, () -> service.createRetrospective(retrospectiveBean));

        
        verifyNoInteractions(repository);
    }
    
    @Test
    void testUpdateRetrospectiveWithValidData() {
        
        Long retrospectiveId = 1L;
        Retrospective existingRetrospective = new Retrospective(retrospectiveId, "Existing Name", "Existing Summary", new Date(), new HashSet<>(), null, "existing-token");
        Retrospective updatedRetrospective = new Retrospective(retrospectiveId, "Updated Name", "Updated Summary", new Date(), new HashSet<>(), null, "updated-token");
        
        when(repository.findById(retrospectiveId)).thenReturn(Optional.of(existingRetrospective));
        when(repository.save(any(Retrospective.class))).thenReturn(updatedRetrospective);
        
        
        Retrospective result = service.updateRetrospective(retrospectiveId, updatedRetrospective);
        
        assertNotNull(result);
        assertEquals("Updated Name", result.getName());
        assertEquals("Updated Summary", result.getSummary());
    }

    @Test
    void testUpdateRetrospectiveWithNonexistentId() {
        
        Long nonexistentId = 2L;
        Retrospective updatedRetrospective = new Retrospective(nonexistentId, "Updated Name", "Updated Summary", new Date(), new HashSet<>(), null, "updated-token");
        
        when(repository.findById(nonexistentId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.updateRetrospective(nonexistentId, updatedRetrospective));

        verify(repository).findById(nonexistentId);
        verifyNoMoreInteractions(repository);
    }
    
    
    @Test
    void testCreateFeedbackWithValidData() {
        
        Long retrospectiveId = 1L;
        Retrospective existingRetrospective = new Retrospective(retrospectiveId, "Existing Name", "Existing Summary", new Date(), new HashSet<>(), new HashSet<>(), "existing-token");
        FeedbackBean feedbackBean = new FeedbackBean("Feedback Name", "Feedback Body", "Feedback Type");
        
        when(repository.findById(retrospectiveId)).thenReturn(Optional.of(existingRetrospective));
        when(repository.save(any(Retrospective.class))).thenReturn(existingRetrospective);

        Retrospective result = service.createFeedback(retrospectiveId, feedbackBean);

        verify(repository).findById(retrospectiveId);
        verify(repository).save(any(Retrospective.class));
        
        assertNotNull(result);
        assertFalse(result.getFeedbacks().isEmpty());
        Feedback createdFeedback = result.getFeedbacks().iterator().next();
        assertEquals("Feedback Name", createdFeedback.getName());
        assertEquals("Feedback Body", createdFeedback.getBody());
        assertEquals("Feedback Type", createdFeedback.getType());
    }

    @Test
    void testCreateFeedbackWithInvalidData() {
        
        Long retrospectiveId = 1L;
        FeedbackBean invalidFeedbackBean = new FeedbackBean(null, null, null);
        
        Retrospective existingRetrospective = new Retrospective(retrospectiveId, "Existing Name", "Existing Summary", new Date(), new HashSet<>(), new HashSet<>(), "existing-token");

        when(repository.findById(retrospectiveId)).thenReturn(Optional.of(existingRetrospective));
        when(repository.save(any(Retrospective.class))).thenAnswer(invocation -> {
            return invocation.getArgument(0);
        });
        
        assertThrows(IncorrectRequestException.class, () -> service.createFeedback(retrospectiveId, invalidFeedbackBean));
    }

    @Test
    void testCreateFeedbackWithNonexistentRetrospective() {
        
        Long nonexistentRetrospectiveId = 2L;
        FeedbackBean feedbackBean = new FeedbackBean("Feedback Name", "Feedback Body", "Feedback Type");
        
        when(repository.findById(nonexistentRetrospectiveId)).thenReturn(Optional.empty());
        
        assertThrows(EntityNotFoundException.class, () -> service.createFeedback(nonexistentRetrospectiveId, feedbackBean));
        
        verify(repository).findById(nonexistentRetrospectiveId);
        verifyNoMoreInteractions(repository);
    }
    
    @Test
    void testGetRetrospectivesWithDateAndComparisonType() {
        
        Date date = new Date();
        String comparisonType = "eq";
        Pageable pageable = Pageable.unpaged();
        Page<Retrospective> expectedPage = new PageImpl<>(List.of(new Retrospective()));
        
        when(repository.findByDateEquals(eq(date), eq(pageable))).thenReturn(expectedPage);
        Page<Retrospective> result = service.getRetrospectives(date, comparisonType, pageable);
        verify(repository).findByDateEquals(eq(date), eq(pageable));
        assertEquals(expectedPage, result);
    }
    
    @Test
    void testUpdateFeedbackWithValidData() {
        
        Long retrospectiveId = 1L;
        Long feedbackId = 1L;
        Retrospective existingRetrospective = new Retrospective(retrospectiveId, "Existing Name", "Existing Summary", new Date(), new HashSet<>(), new HashSet<>(), "existing-token");
        Feedback existingFeedback = new Feedback(feedbackId, "Existing Feedback Name", "Existing Feedback Body", "Existing Feedback Type", "existing-feedback-token");
        existingRetrospective.getFeedbacks().add(existingFeedback);

        FeedbackBean updatedFeedbackBean = new FeedbackBean("Updated Feedback Name", "Updated Feedback Body", "Updated Feedback Type");

        when(repository.getReferenceById(retrospectiveId)).thenReturn(existingRetrospective);
        when(repository.save(any(Retrospective.class))).thenReturn(existingRetrospective);
        
        Retrospective result = service.updateFeedback(retrospectiveId, feedbackId, updatedFeedbackBean);
        
        verify(repository).getReferenceById(retrospectiveId);
        verify(repository).save(any(Retrospective.class));

        assertNotNull(result);
        assertFalse(result.getFeedbacks().isEmpty());
        Feedback updatedFeedback = result.getFeedbacks().iterator().next();
        assertEquals("Updated Feedback Name", updatedFeedback.getName());
        assertEquals("Updated Feedback Body", updatedFeedback.getBody());
        assertEquals("Updated Feedback Type", updatedFeedback.getType());
    }

    @Test
    @DisplayName("Update feedback with invalid data")
    void testUpdateFeedbackWithInvalidData() {
        
        Long retrospectiveId = 1L;
        Long feedbackId = 1L;
        FeedbackBean invalidFeedbackBean = new FeedbackBean(null, null, null);
        Retrospective existingRetrospective = new Retrospective(retrospectiveId, "Existing Name", "Existing Summary", new Date(), new HashSet<>(), new HashSet<>(), "existing-token");
        Feedback existingFeedback = new Feedback(feedbackId, "Existing Feedback Name", "Existing Feedback Body", "Existing Feedback Type", "existing-feedback-token");
        existingRetrospective.getFeedbacks().add(existingFeedback);

        when(repository.getReferenceById(retrospectiveId)).thenReturn(existingRetrospective);
        when(repository.save(any(Retrospective.class))).thenAnswer(invocation -> {
            return invocation.getArgument(0);
        });
        
        
        assertThrows(IncorrectRequestException.class, () -> service.updateFeedback(retrospectiveId, feedbackId, invalidFeedbackBean));

    }

    @Test
    @DisplayName("Should get retro by id")
    void testGetRetrospectiveById() {
        
        Long id = 1L;
        Retrospective expectedRetrospective = new Retrospective();
        when(repository.findById(id)).thenReturn(Optional.of(expectedRetrospective));
        
        Retrospective resultRetrospective = service.getRetrospectiveById(id);
        
        assertEquals(expectedRetrospective, resultRetrospective);
        verify(repository).findById(id);
    }
}
