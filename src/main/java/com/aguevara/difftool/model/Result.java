package com.aguevara.difftool.model;

import com.aguevara.difftool.services.DiffResult;

import java.util.Objects;

public class Result {
    public String description;
    public DiffResult result;

    public Result() {}

    public Result(String description, DiffResult result) {
        this.description = description;
        this.result = result;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public DiffResult getResult() {
        return result;
    }

    public void setResult(DiffResult result) {
        this.result = result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Result comparedResult = (Result)o;
        if (comparedResult.getDescription() == null && this.description == null) {
            return comparedResult.getResult().equals(this.result);
        }
        if (comparedResult.getDescription() == null && this.description != null) {
            return false;
        }
        if (comparedResult.getDescription() != null) {
            return comparedResult.getDescription().equals(this.description) &&
                    comparedResult.getResult().equals(this.result);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return 31 + Objects.hashCode(description) + Objects.hashCode(result);
    }
}
