package dev.iitp.subscriber.model.feature;

import lombok.*;

@ToString
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class HeartRateFeature {

    private double mean;
    private double std;
}
