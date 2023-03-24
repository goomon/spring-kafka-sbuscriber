package dev.iitp.subscriber.model.cache;

import dev.iitp.subscriber.model.Axis;
import dev.iitp.subscriber.model.SensorRecord;
import dev.iitp.subscriber.model.feature.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Queue;
import java.util.function.Supplier;
import java.util.stream.Stream;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class SensorWindowQueue {

    private long timestamp;
    private long lastExtracted;
    private Queue<SensorRecord> sensorRecordQueue;

    public void push(SensorRecord record) {
        sensorRecordQueue.add(record);
    }

    public void pop() {
        sensorRecordQueue.poll();
    }

    public int getQueueSize() {
        return sensorRecordQueue.size();
    }

    public SensorRecordFeature extractFeature(String userId) {
        Supplier<Stream<SensorRecord>> rawStreamSupplier = () -> sensorRecordQueue.stream();

        // Accelerometer mean, variance
        Supplier<Stream<Axis>> axisStreamSupplier = () -> rawStreamSupplier.get().flatMap(record -> record.getValue().getAcc().getValue().stream());
        double accXMean = axisStreamSupplier.get().mapToDouble(Axis::getX).average().getAsDouble();
        double accXVar = axisStreamSupplier.get().mapToDouble(x -> x.getX() - accXMean).map(x -> x * x).average().getAsDouble();
        double accYMean = axisStreamSupplier.get().mapToDouble(Axis::getY).average().getAsDouble();
        double accYVar = axisStreamSupplier.get().mapToDouble(x -> x.getY() - accYMean).map(x -> x * x).average().getAsDouble();
        double accZMean = axisStreamSupplier.get().mapToDouble(Axis::getZ).average().getAsDouble();
        double accZVar = axisStreamSupplier.get().mapToDouble(x -> x.getZ() - accZMean).map(x -> x * x).average().getAsDouble();

        // BloodVolumePressure mean, variance
        Supplier<Stream<Double>> bvpStreamSupplier = () -> rawStreamSupplier.get().flatMap(record -> record.getValue().getBvp().getValue().stream());
        double bvpMean = bvpStreamSupplier.get().mapToDouble(x -> x).average().getAsDouble();
        double bvpVar = bvpStreamSupplier.get().map(x -> x - bvpMean).map(x -> x * x).mapToDouble(x -> x).average().getAsDouble();

        // ElectrodernalActivity mean, variance
        Supplier<Stream<Double>> edaStreamSupplier = () -> rawStreamSupplier.get().flatMap(record -> record.getValue().getEda().getValue().stream());
        double edaMean = edaStreamSupplier.get().mapToDouble(x -> x).average().getAsDouble();
        double edaVar = edaStreamSupplier.get().map(x -> x - edaMean).map(x -> x * x).mapToDouble(x -> x).average().getAsDouble();

        // HeatRate mean, variance
        Supplier<Stream<Double>> hrStreamSupplier = () -> rawStreamSupplier.get().flatMap(record -> record.getValue().getHr().getValue().stream());
        double hrMean = hrStreamSupplier.get().mapToDouble(x -> x).average().getAsDouble();
        double hrVar = hrStreamSupplier.get().map(x -> x - hrMean).map(x -> x * x).mapToDouble(x -> x).average().getAsDouble();

        // Temperature mean, variance
        Supplier<Stream<Double>> tempStreamSupplier = () -> rawStreamSupplier.get().flatMap(record -> record.getValue().getTemp().getValue().stream());
        double tempMean = tempStreamSupplier.get().mapToDouble(x -> x).average().getAsDouble();
        double tempVar = tempStreamSupplier.get().map(x -> x - tempMean).map(x -> x * x).mapToDouble(x -> x).average().getAsDouble();

        lastExtracted = timestamp;
        return new SensorRecordFeature(null, userId, timestamp, new SensorRecordFeature.Value(
                new AccelerometerFeature(accXMean, accYMean, accZMean, accXVar, accYVar, accZVar),
                new BloodVolumePressureFeature(bvpMean, bvpVar),
                new ElectrodernalActivityFeature(edaMean, edaVar),
                new HeartRateFeature(hrMean, hrVar),
                new TemperatureFeature(tempMean, tempVar)
        ));
    }

}
