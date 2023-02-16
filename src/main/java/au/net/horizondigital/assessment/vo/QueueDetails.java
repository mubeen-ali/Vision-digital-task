package au.net.horizondigital.assessment.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class QueueDetails {
    @JsonProperty("queue_size")
    private int queueSize;

    @JsonProperty("no_of_waiting_customers")
    private int noOfWaitingCustomers;

}
