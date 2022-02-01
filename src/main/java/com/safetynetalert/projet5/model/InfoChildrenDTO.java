package com.safetynetalert.projet5.model;

import lombok.*;
import java.util.List;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class InfoChildrenDTO {

    private List<Person> personList;
    private long ageChildren;
    private long ageAdult;

}
