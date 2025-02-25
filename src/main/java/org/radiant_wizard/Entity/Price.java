package org.radiant_wizard.Entity;

import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@ToString
public class Price {
    LocalDateTime modificationDate;
    Double value;

    public Price(LocalDateTime modificationDate, Double value) {
        this.modificationDate = modificationDate;
        this.value = value;
    }
}
