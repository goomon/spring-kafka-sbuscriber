package dev.iitp.subscriber.model.feature;

import lombok.*;

@ToString
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class TemperatureFeature {

    private double mean;
    private double std;
}
