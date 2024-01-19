package in.simplygeek.restrospective.beans;

import java.util.Date;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RetrospectiveBean {
	private Long id;
	private String name;
	private String summary;
	private Date date;
    private Set<String> participants;
}