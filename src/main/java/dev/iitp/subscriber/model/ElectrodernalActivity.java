package dev.iitp.subscriber.model;

import lombok.*;

import java.util.List;

@ToString
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class ElectrodernalActivity {

    private int hz;
    private List<Double> value;
}
