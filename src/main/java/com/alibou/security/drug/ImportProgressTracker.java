package com.alibou.security.drug;

import lombok.Data;
import lombok.Getter;
import org.springframework.stereotype.Component;

@Getter
@Component
@Data
public class ImportProgressTracker {

    private volatile int progress = 0;
    private volatile boolean completed = false;

    public void reset() {
        this.progress = 0;
        this.completed = false;
    }

    public void update(int progress) {
        this.progress = progress;
    }

    public void complete() {
        this.progress = 100;
        this.completed = true;
    }

}
