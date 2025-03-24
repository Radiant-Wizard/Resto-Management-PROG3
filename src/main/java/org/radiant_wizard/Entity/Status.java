package org.radiant_wizard.Entity;

import lombok.Data;
import lombok.Getter;
import lombok.ToString;
import org.radiant_wizard.Entity.Enum.StatusType;

import java.time.Instant;

@Getter
@ToString
public class Status {
    private final StatusType statusType;
    private final Instant creationDate;

    public Status(StatusType statusType, Instant creationDate){
        this.statusType = statusType;
        this.creationDate = creationDate;
    }
}
