package in.simplygeek.retrospective.beans;

import java.util.Date;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class RetrospectiveBean {
	private Long id;
	private String name;
	private String summary;
	private Date date;
    private Set<String> participants;
}