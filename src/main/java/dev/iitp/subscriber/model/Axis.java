package dev.iitp.subscriber.model;

import lombok.*;

@ToString
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class Axis {

    private double x;
    private double y;
    private double z;

    public static Axis add(Axis a1, Axis a2) {
        Axis axis = new Axis();
        axis.setX(a1.getX() + a2.getX());
        axis.setY(a1.getY() + a2.getY());
        axis.setZ(a1.getZ() + a2.getZ());
        return axis;
    }
}
