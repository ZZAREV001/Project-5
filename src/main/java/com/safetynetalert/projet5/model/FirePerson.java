package com.safetynetalert.projet5.model;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
@JsonView(View.FilterFireEndpoints.class)
public class FirePerson {

    private List<FullInfoPerson> persons;

}
