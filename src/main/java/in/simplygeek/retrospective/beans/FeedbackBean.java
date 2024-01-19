package in.simplygeek.retrospective.beans;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class FeedbackBean {

    private String name;

    private String body;

    private String type;
}