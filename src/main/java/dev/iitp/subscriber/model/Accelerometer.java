package dev.iitp.subscriber.model;

import lombok.*;

import java.util.List;

@ToString
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class Accelerometer {

    private int hz;
    private List<Axis> value;
}
