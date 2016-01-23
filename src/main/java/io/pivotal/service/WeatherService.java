package io.pivotal.service;

import io.pivotal.Constants;
import io.pivotal.errorHandling.TooManyRequestsException;
import io.pivotal.model.Coordinate;
import org.joda.time.DateTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import retrofit.RetrofitError;

import java.net.UnknownServiceException;
import java.util.Date;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by pivotal on 1/6/16.
 */
@Component
public class WeatherService {

    @Autowired
    IWundergroundService service;

    private Queue<Long> apiTimeStamps = new ArrayBlockingQueue<>(Constants.REQUEST_LIMIT);

    private void ensureApiQuota() throws TooManyRequestsException {
        long now = DateTimeUtils.currentTimeMillis();
        synchronized (apiTimeStamps) {
            if (apiTimeStamps.size() < Constants.REQUEST_LIMIT) {
                apiTimeStamps.add(now);
            } else {
                if (apiTimeStamps.peek() < (now - Constants.REQUEST_PERIOD_MILLISECONDS)) {
                    apiTimeStamps.remove();
                    apiTimeStamps.add(now);
                } else {
                    throw new TooManyRequestsException();
                }
            }
        }
    }

    public double getCurrentTemp(Coordinate coordinate) throws Exception {
        try {
            ensureApiQuota();

            return service
                    .getConditionsResponse(Double.toString(coordinate.getLatitude()),
                            Double.toString(coordinate.getLongitude()))
                    .getTempF();
        }
        catch (RetrofitError e) {
            e.printStackTrace();
            throw new UnknownServiceException(e.getMessage());
        }
    }

    public Map<Date, Double> getFutureTemp(Coordinate coordinate) throws Exception {
        try {
            ensureApiQuota();

            return service
                    .getForecastResponse(Double.toString(coordinate.getLatitude()),
                            Double.toString(coordinate.getLongitude()))
                    .getTemps();
        } catch (RetrofitError e) {
            e.printStackTrace();
            throw new UnknownServiceException(e.getMessage());
        }
    }
}
