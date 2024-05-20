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
        double temp = IntegralCalculatorGUI.calculateIntegral(lowerLimit, upperLimit, step);
        integralResult += temp;
    }
}