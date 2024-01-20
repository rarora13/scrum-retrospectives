package in.simplygeek.retrospective.service;

import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import in.simplygeek.retrospective.beans.FeedbackBean;
import in.simplygeek.retrospective.beans.RetrospectiveBean;
import in.simplygeek.retrospective.entities.Retrospective;


public interface RetrospectiveService {
	
	/**
	 * Get all Retrospective
	 * 
	 * @param date
	 * @param comparisonType
	 * @param pageable
	 * @return
	 */
	Page<Retrospective> getRetrospectives(Date date, String comparisonType, Pageable pageable);
    
    /**
     * Get the retrospective by id
     * 
     * @param id
     * @return
     */
    Retrospective getRetrospectiveById(Long id);

	/**
	 * Method to create a new Retrospective
	 * 
	 * @param retrospective
	 * @return
	 */
	Retrospective createRetrospective(RetrospectiveBean retrospective);
	
	/**
	 * This will update a given Retrospective
	 * 
	 * @param id
	 * @param retrospective
	 * @return
	 */
	Retrospective updateRetrospective(Long id, Retrospective retrospective);
	
	/**
	 * This will create a new Feedback item
	 * 
	 * @param retroId
	 * @param feedbackBean
	 * @return
	 */
	Retrospective createFeedback(Long retroId, FeedbackBean feedbackBean);
	
	/**
	 * This will update the feedback item by given retrospective
	 * 
	 * @param retroId
	 * @param feedbackId
	 * @param feedbackBean
	 * @return
	 */
	Retrospective updateFeedback(Long retroId,Long feedbackId, FeedbackBean feedbackBean);

	

	/**
	 * This method will delete the Retrospective
	 * 
	 * @param id
	 */
	void deleteRetrospective(Long id);
}
