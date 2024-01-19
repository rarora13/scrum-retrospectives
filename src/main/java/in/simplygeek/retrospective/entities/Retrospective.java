package in.simplygeek.retrospective.entities;

import java.util.Date;
import java.util.Set;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Retrospective {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column
	private String name;
	
	@Column
	private String summary;

	@Column
	private Date date;
	
	@OneToMany
    @Cascade(CascadeType.ALL)
    private Set<Participant> participants;
	
	@OneToMany
    @Cascade(CascadeType.ALL)
    private Set<Feedback> feedbacks;
	
	@Column
	private String retroUniqueId;
}