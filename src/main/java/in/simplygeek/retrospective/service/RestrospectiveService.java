package in.simplygeek.retrospective.service;

import java.util.Date;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import in.simplygeek.retrospective.beans.FeedbackBean;
import in.simplygeek.retrospective.beans.RetrospectiveBean;
import in.simplygeek.retrospective.entities.Feedback;
import in.simplygeek.retrospective.entities.Participant;
import in.simplygeek.retrospective.entities.Retrospective;
import in.simplygeek.retrospective.exceptions.IncorrectRequestException;
import in.simplygeek.retrospective.repository.RestrospectiveRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class RestrospectiveService {
    private final RestrospectiveRepository repository;
    
    @Autowired
    public RestrospectiveService(RestrospectiveRepository retrospectiveRepository) {
        this.repository = retrospectiveRepository;
    }


	/**
	 * Get all Retrospective
	 * 
	 * @param date
	 * @param comparisonType
	 * @param pageable
	 * @return
	 */
	public Page<Retrospective> getRetrospectives(Date date, String comparisonType, Pageable pageable) {
		if(date != null) {
			switch (comparisonType.toLowerCase()) {
			case "eq":
				return repository.findByDateEquals(date, pageable);
			case "gt":
				return repository.findByDateGreaterThan(date, pageable);
			case "lt":
				return repository.findByDateLessThan(date, pageable);
			default:
				return repository.findByDateEquals(date, pageable);
			}
		}
		return repository.findAll(pageable);
		
	}
    
    /**
     * Get the retrospective by id
     * 
     * @param id
     * @return
     */
    public Retrospective getRetrospectiveById(Long id) {
		return repository.findById(id).
				orElseThrow(()-> new EntityNotFoundException("Retrospective not found with id :"+id));
	}

	/**
	 * Method to create a new Retrospective
	 * 
	 * @param retrospective
	 * @return
	 */
	public Retrospective createRetrospective(RetrospectiveBean retrospective) {
		if(retrospective != null && retrospective.getDate() != null && retrospective.getName() != null &&retrospective.getParticipants() != null 
				&& !retrospective.getName().isBlank() && retrospective.getParticipants().size() > 0 ) {
			Set<Participant> participants = retrospective.getParticipants().stream().map((p)-> {return new Participant(null, p, UUID.randomUUID().toString());}).collect(Collectors.toSet());
			Retrospective entity = new Retrospective(null, retrospective.getName(), retrospective.getSummary(), retrospective.getDate(), participants, null, UUID.randomUUID().toString());
			
			return repository.save(entity);
		}
		throw new IncorrectRequestException("Incomplete Retrospective details");
		
	}
	
	/**
	 * This will update a given Retrospective
	 * 
	 * @param id
	 * @param retrospective
	 * @return
	 */
	public Retrospective updateRetrospective(Long id, Retrospective retrospective) {
		Retrospective existingretrospective = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("retrospective not found with id: " + id));

		/* Update the existing retrospective properties */
		existingretrospective.setName(retrospective.getName());
		existingretrospective.setDate(retrospective.getDate());
		existingretrospective.setSummary(retrospective.getSummary());
        

		/* Save the updated retrospective */
        return repository.save(existingretrospective);
	}
	
	/**
	 * This will create a new Feedback item
	 * 
	 * @param retroId
	 * @param feedbackBean
	 * @return
	 */
	public Retrospective createFeedback(Long retroId, FeedbackBean feedbackBean) {
		
		Retrospective retro = null;
		if(retroId != null && retroId > 0) {
			retro = repository.findById(retroId)
	                .orElseThrow(() -> new EntityNotFoundException("retrospective not found with id: " + retroId));
			if(retro == null) {
				throw new IncorrectRequestException("Retrospective Id not found");
			}
		}
		if(feedbackBean != null && feedbackBean.getName() != null && feedbackBean.getBody() != null && feedbackBean.getType() != null 
				&&!feedbackBean.getName().isBlank() && !feedbackBean.getBody().isBlank() && !feedbackBean.getType().isBlank()) {
			
			retro.getFeedbacks().add(new Feedback(null,feedbackBean.getName(), feedbackBean.getBody(), feedbackBean.getType(), UUID.randomUUID().toString()));
			
			return repository.save(retro);
		}
		throw new IncorrectRequestException("Incomplete Feedback details");
		
	}
	
	/**
	 * This will update the feedback item by given retrospective
	 * 
	 * @param retroId
	 * @param feedbackId
	 * @param feedbackBean
	 * @return
	 */
	public Retrospective updateFeedback(Long retroId,Long feedbackId, FeedbackBean feedbackBean) {

		Retrospective retro = null;
		Feedback feedback = null;
		if(retroId != null && retroId > 0) {
			retro = repository.getReferenceById(retroId);
			if(retro != null) {
				if(feedbackId != null && feedbackId > 0) {
					Optional<Feedback> oFeedback = retro.getFeedbacks().stream().filter((f)-> (f.getId() == feedbackId)).findAny();
					if(oFeedback.isPresent()) {
						feedback = oFeedback.get();
						if(feedbackBean != null) {
							boolean updateFlag = false;
							if(feedbackBean.getName() != null && !feedbackBean.getName().isBlank()) {
								feedback.setName(feedbackBean.getName());
								updateFlag = true;
							}
							if (feedbackBean.getBody() != null && !feedbackBean.getBody().isBlank()) {
								feedback.setBody(feedbackBean.getBody());
								updateFlag = true;
							}
							if (feedbackBean.getType() != null && !feedbackBean.getType().isBlank()) {
								feedback.setType(feedbackBean.getType());
								updateFlag = true;
							}
							if (updateFlag) {
								return repository.save(retro);
							}else {
								throw new IncorrectRequestException("No Details are provided to update");
							}
							
						}
						throw new IncorrectRequestException("Incomplete Feedback details");
						
					}else {
						throw new EntityNotFoundException("Feedback not found with id :"+feedbackId);
					}
				}else {
					throw new IncorrectRequestException("Feedback id is not provided");
				}
			}else {
				throw new IncorrectRequestException("Retrospective not found");
			}
			
		}

		throw new IncorrectRequestException("Retrospective Id not correct");
		

	}

	

	public void deleteRetrospective(Long id) {
		/* Check if the retrospective with the given id exists in the database */
        Retrospective existingretrospective = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("retrospective not found with id: " + id));

		/* Delete the retrospective */
        repository.delete(existingretrospective);
	}
}
