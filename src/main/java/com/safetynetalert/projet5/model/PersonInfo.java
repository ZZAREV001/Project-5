package com.safetynetalert.projet5.model;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@EqualsAndHashCode
@JsonView(View.FilterPersonInfoEndpoints.class)
public class PersonInfo {

    private List<FullInfoPerson> personsInfo;

}
