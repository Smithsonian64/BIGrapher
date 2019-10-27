import java.awt.*;
import java.awt.image.BufferedImage;

public class MathFunction {

    public static double step = 0.01;
    public static double yScaleFactor = 0.05;
    public static double xScaleFactor = 0.05;
    public static double thetaScaleFactor = 1;
    public static double rScaleFactor = 0.1;
    public static double angleLimit = Math.PI * 7;
    public static int functionNumber = 0;
    public static int maxNumOfCartesianFunctions = 10;
    public static int maxNumOfPolarFunctions = 10;
    public static String currentFunctionText;
    public static final int CARTESIAN = 0;
    public static final int POLAR = 1;
    public static int coordinateSystem = CARTESIAN;

    public static BufferedImage drawCurve(BufferedImage background) {
        long startTime;
        long endTime;
        long totalTime;
        int rgb;
        int points;
        double absoluteX;
        int absoluteY;
        int tempAbsoluteX;
        BufferedImage tempImage = background;

        if(coordinateSystem == CARTESIAN) {
            startTime = System.nanoTime();
            rgb = new Color(255, 255, 255).getRGB();

            absoluteX = background.getWidth() / 2;
            absoluteY = background.getHeight() / 2;



            for (int i = 0; i < background.getWidth(); i++) {
                tempImage.setRGB(i, absoluteY, rgb);
                tempImage.setRGB((int) absoluteX, i, rgb);
            }

            absoluteX = 0 - background.getWidth() / 2;
            points = 0;

            rgb = new Color(255, 255, 0).getRGB();

            while (absoluteX < background.getWidth()) {
                absoluteY = getCartesianY(absoluteX);
                tempAbsoluteX = (int) (Math.round(absoluteX) + background.getWidth() / 2);
                if (absoluteY >= 0 && absoluteY < background.getHeight() && tempAbsoluteX >= 0 && tempAbsoluteX < background.getWidth()) {
                    tempImage.setRGB(tempAbsoluteX, absoluteY, rgb);
                    points++;
                }
                absoluteX += step;
            }

            endTime = System.nanoTime();
            totalTime = endTime - startTime;
            System.out.println("Plotted " + points + " points in " + totalTime / 1000000 + " milliseconds.");

            return tempImage;
        }

        if(coordinateSystem == POLAR) {
            startTime = System.nanoTime();


            points = 0;
            double currentAngle = 0;

            rgb = new Color(255, 255, 255).getRGB();

            absoluteX = background.getWidth() / 2;
            absoluteY = background.getHeight() / 2;

            for (int i = 0; i < background.getWidth(); i++) {
                tempImage.setRGB(i, absoluteY, rgb);
                tempImage.setRGB((int) absoluteX, i, rgb);
            }

            absoluteX = background.getWidth() / 2;
            absoluteY = background.getHeight() / 2;

            rgb = new Color(255, 255, 0).getRGB();

            while(currentAngle <= angleLimit) {
                absoluteY = getPolarY(currentAngle);
                absoluteX = (PolarMathFunction(currentAngle) / rScaleFactor) * Math.cos(currentAngle);
                tempAbsoluteX = (int) (Math.round(absoluteX) + background.getWidth() / 2);
                if (absoluteY >= 0 && absoluteY < background.getHeight() && tempAbsoluteX >= 0 && tempAbsoluteX < background.getWidth()) {
                    tempImage.setRGB(tempAbsoluteX, absoluteY, rgb);
                    points++;
                }
                currentAngle += step;
            }

            endTime = System.nanoTime();
            totalTime = endTime - startTime;
            System.out.println("Plotted " + points + " points in " + totalTime / 1000000 + " milliseconds.");
            return tempImage;

        }

        return tempImage;

    }

    public static double PolarMathFunction(double PreTheta) {
        double theta = PreTheta * thetaScaleFactor;
        double output = theta;

        switch (functionNumber) {
            case 0:
                output = (theta + 2 * Math.sin(2 * Math.PI * theta) + 4 * Math.cos(2 * Math.PI * theta));
                currentFunctionText = "theta + 2 * sin(2 * pi  theta) + 4 * cos(2 * pi * theta)";
                break;
            case 1:
                output = Math.cos(.95*theta);
                currentFunctionText = "cos(0.95 * theta)";
                break;
            case 2:
                output = theta + 3*Math.sin(4*theta) - 5*Math.cos(4*theta);
                currentFunctionText = "theta + 3 * sin(4 * theta) - 5 * cos(4 * theta)";
                break;
            case 3:
                output = Math.sin(Math.pow(2, theta)) - 1.7;
                currentFunctionText = "sin(2^theta) - 1.7";
                break;
            case 4:
                output = Math.cos(theta) - 1;
                currentFunctionText = "cos(theta)";
                break;
            case 5:
                output = Math.tan(theta);
                currentFunctionText = "tan(theta)";
                break;
            case 6:
                output = 1 / Math.tan(Math.pow(theta, Math.sin(theta)));
                currentFunctionText = "1 / tan(theta^sin(theta))";
                break;
            case 7:
                output = ((Math.sin(theta)*Math.sqrt(Math.abs(Math.cos(theta))))/(Math.sin(theta) + 7/5)) - 2*Math.sin(theta) + 2;
                currentFunctionText = "(sin(theta) * sqrt(abs(cos(theta))) / sin(theta) + 7/5) - 2 * sin(theta) + 2";
                break;
            case 8:
                output = Math.sin(2*theta);
                currentFunctionText = "sin(2 * theta)";
                break;
            case 9:
                output = Math.cbrt(Math.log(theta));
                currentFunctionText = "(log(theta))^1/3";
                break;
            case 10:
                output = Math.pow(2, Math.cbrt(theta));
                break;
            default:
                output = theta;
        }

        return output;

    }

    public static double CartesianMathFunction(double PreX) {

        double output;
        double x = PreX * xScaleFactor;
        switch (functionNumber) {
            case 0:
                output = Math.tan(x);
                currentFunctionText = "y = tan(x)";
                break;
            case 1:
                output = Math.sin(x);
                currentFunctionText = "y = sin(x)";
                break;
            case 2:
                output = 1 / x;
                currentFunctionText = "y = 1 / x";
                break;
            case 3:
                output = 1 / Math.sin(x);
                currentFunctionText = "y = 1 / sin(x)";
                break;
            case 4:
                output = Math.exp(x);
                currentFunctionText = "y = e^x";
                break;
            case 5:
                output = Math.cbrt(x);
                currentFunctionText = "y = x^1/3";
                break;
            case 6:
                output = (Math.random() * 800) - Window.WINDOW_HEIGHT / 2;
                currentFunctionText = "y = Random";
                break;
            case 7:
                output = Math.log10(x);
                currentFunctionText = "y = log(x)";
                break;
            case 8:
                output = Math.round(x);
                currentFunctionText = "y = Round of x";
                break;
            case 9:
                output = Math.pow(x, 2);
                currentFunctionText = "y = x^2";
                break;
            case 10:
                output = Math.hypot(Math.sin(x), Math.tanh(x));
                currentFunctionText = "y = (sin(x)^2 + tan(x)^2)^1/2";
                break;
            default:
                output = x;
        }

        return output;

    }

    public static int getCartesianY(double x){
        return (int) (((Window.WINDOW_HEIGHT / 2) - CartesianMathFunction(x) / yScaleFactor));
    }

    public static int getPolarY(double theta) {
        return (int) (((Window.WINDOW_HEIGHT / 2) - ((PolarMathFunction(theta) / rScaleFactor) * Math.sin(theta))));
    }


}
