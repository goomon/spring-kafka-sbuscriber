package dev.iitp.subscriber.model.feature;

import lombok.*;

@ToString
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class ElectrodernalActivityFeature {

    private double mean;
    private double std;
}
