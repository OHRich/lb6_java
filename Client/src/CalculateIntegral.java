public class CalculateIntegral implements Runnable {
    private final double lowerLimit;
    private final double upperLimit;
    private final double step;
    private static double integralResult = 0.0;
    public CalculateIntegral(double lowerLimit, double upperLimit, double step){
        this.lowerLimit = lowerLimit;
        this.upperLimit = upperLimit;
        this.step = step;
    }
    public static double getIntegralResult() {
        return integralResult;
    }
    public static void setIntegralResultNull(){
        integralResult = 0.0;
    }

    @Override
    public void run(){
        double temp = calculateIntegral(lowerLimit, upperLimit, step);
        integralResult += temp;
    }

    public static double calculateIntegral(double lowerLimit, double upperLimit, double step) {
        double x1, x2, sum = 0;
        int amountSteps = (int) ((upperLimit - lowerLimit) / step);
        x1 = lowerLimit;

        for (int i = 0; i < amountSteps; i++) {
            x2 = x1 + step;
            sum += 0.5 * step * (Math.cos(x1 * x1) + Math.cos(x2 * x2));
            x1 = x2;
        }
        if ((upperLimit - lowerLimit) % step != 0)
            sum += 0.5 * (upperLimit - x1) * (Math.cos(x1 * x1) + Math.cos(upperLimit * upperLimit));

        return sum;
    }
}