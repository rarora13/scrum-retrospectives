package in.simplygeek.retrospective.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
public class FeedbackBean {

    private String name;

    private String body;

    private String type;
}