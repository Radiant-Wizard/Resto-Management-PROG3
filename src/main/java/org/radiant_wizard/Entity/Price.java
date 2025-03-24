package org.radiant_wizard.Entity;

import lombok.Data;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Data
@ToString
public class Price {
    LocalDateTime modificationDate;
    Double value;

    public Price(LocalDateTime modificationDate, Double value) {
        this.modificationDate = modificationDate;
        this.value = value;
    }
}
