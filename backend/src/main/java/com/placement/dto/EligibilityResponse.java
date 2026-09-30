package com.placement.dto;

import java.util.List;

public class EligibilityResponse {

    private boolean eligible;
    private String reason;
    private List<CriterionCheck> checks;

    public static class CriterionCheck {
        private String criterion;
        private String required;
        private String actual;
        private boolean passed;

        public CriterionCheck() {}

        public CriterionCheck(String criterion, String required, String actual, boolean passed) {
            this.criterion = criterion;
            this.required = required;
            this.actual = actual;
            this.passed = passed;
        }

        public String getCriterion() { return criterion; }
        public void setCriterion(String criterion) { this.criterion = criterion; }

        public String getRequired() { return required; }
        public void setRequired(String required) { this.required = required; }

        public String getActual() { return actual; }
        public void setActual(String actual) { this.actual = actual; }

        public boolean isPassed() { return passed; }
        public void setPassed(boolean passed) { this.passed = passed; }
    }

    public EligibilityResponse() {}

    public EligibilityResponse(boolean eligible, String reason, List<CriterionCheck> checks) {
        this.eligible = eligible;
        this.reason = reason;
        this.checks = checks;
    }

    public boolean isEligible() { return eligible; }
    public void setEligible(boolean eligible) { this.eligible = eligible; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public List<CriterionCheck> getChecks() { return checks; }
    public void setChecks(List<CriterionCheck> checks) { this.checks = checks; }
}
