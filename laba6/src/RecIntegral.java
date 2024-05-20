import java.io.Serializable;

class RecIntegral implements Serializable {
    private double lowerLimit;
    private double upperLimit;

    private double step;


    public RecIntegral(double lowerLimit, double upperLimit, double step) throws ExceptInvalidValues {
        this.lowerLimit = lowerLimit;
        this.upperLimit = upperLimit;
        this.step = step;

    }

    public double getLowerLimit() {
        return lowerLimit;
    }

    public double getUpperLimit() {
        return upperLimit;
    }

    public double getStep() {
        return step;
    }

    public void setLowerLimit(String lowerLimit) {

        this.lowerLimit = Double.parseDouble(lowerLimit);
    }

    public void setUpperLimit(String upperLimit) {

        this.upperLimit = Double.parseDouble(upperLimit);

    }

    public void setStep(String step) {

        this.step = Double.parseDouble(step);

    }
}