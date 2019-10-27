import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.concurrent.TimeUnit;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class Window implements ActionListener, Runnable {

    final static int WINDOW_WIDTH = 800;
    final static int WINDOW_HEIGHT = 800;

    JFrame frame = new JFrame("Graph");

    BufferedImage graph;
    GraphPanel graphPanel;
    JToolBar optionsToolbar;
    JSpinner stepSizeSpinner;
    JSpinner xScaleFactorSpinner;
    JSpinner yScaleFactorSpinner;
    JLabel stepsize;
    JLabel xFactor;
    JLabel yFactor;
    JLabel currentFunction;
    JLabel angleLimit;
    JSpinner angleLimitSpinner;
    boolean animating = false;

    public void run() {

    }

    public Window() {
        frame.setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        frame.setResizable(false);
        graph = MathFunction.drawCurve(new BufferedImage(WINDOW_WIDTH, WINDOW_HEIGHT, BufferedImage.TYPE_INT_RGB));
        optionsToolbar = new JToolBar();
        optionsToolbar.setFloatable(false);

        stepsize = new JLabel("Step Size: ");
        xFactor = new JLabel("x Factor: ");
        yFactor = new JLabel("y Factor: ");

        angleLimit = new JLabel("Angle Limit: ");

        SpinnerModel angleLimitSpinnerModel = new SpinnerNumberModel(MathFunction.angleLimit, Math.PI / 16, Math.PI * 10, Math.PI / 64);
        angleLimitSpinner = new JSpinner(angleLimitSpinnerModel);
        optionsToolbar.add(angleLimit);
        optionsToolbar.add(angleLimitSpinner);
        optionsToolbar.addSeparator();

        angleLimit.setVisible(false);
        angleLimitSpinner.setVisible(false);

        SpinnerModel stepSizeSpinnerModel = new SpinnerNumberModel(MathFunction.step, 0.0001, 5, 0.001);
        stepSizeSpinner = new JSpinner(stepSizeSpinnerModel);
        optionsToolbar.add(stepsize);
        optionsToolbar.add(stepSizeSpinner);
        optionsToolbar.addSeparator();

        SpinnerModel xScaleFactorSpinnerModel = new SpinnerNumberModel(MathFunction.xScaleFactor, 0.0001, 1, 0.001);
        xScaleFactorSpinner = new JSpinner(xScaleFactorSpinnerModel);
        optionsToolbar.add(xFactor);
        optionsToolbar.add(xScaleFactorSpinner);
        optionsToolbar.addSeparator();

        SpinnerModel yScaleFactorSpinnerModel = new SpinnerNumberModel(MathFunction.yScaleFactor, 0.0001, 1, 0.001);
        yScaleFactorSpinner = new JSpinner(yScaleFactorSpinnerModel);
        optionsToolbar.add(yFactor);
        optionsToolbar.add(yScaleFactorSpinner);

        JButton animateButton = new JButton("Animate");
        animateButton.addActionListener(this);
        animateButton.setActionCommand("Animate");
        optionsToolbar.add(animateButton);

        JButton changeButton = new JButton("Change");
        changeButton.addActionListener(this);
        changeButton.setActionCommand("Change");
        optionsToolbar.add(changeButton);

        JButton coordinateSystemButton = new JButton("System");
        coordinateSystemButton.addActionListener(this);
        coordinateSystemButton.setActionCommand("System");
        optionsToolbar.add(coordinateSystemButton);

        stepSizeSpinner.addChangeListener(e -> {
                MathFunction.step = (double) ((JSpinner) e.getSource()).getValue();
                reDrawCurve();
                return;
        });

        xScaleFactorSpinner.addChangeListener(e -> {
            if(MathFunction.coordinateSystem == MathFunction.CARTESIAN) {
                MathFunction.xScaleFactor = (double) ((JSpinner) e.getSource()).getValue();
                reDrawCurve();
                return;
            }
            if(MathFunction.coordinateSystem == MathFunction.POLAR) {
                MathFunction.thetaScaleFactor = (double) ((JSpinner) e.getSource()).getValue();
                reDrawCurve();
                return;
            }
        });

        yScaleFactorSpinner.addChangeListener(e -> {
            if(MathFunction.coordinateSystem == MathFunction.CARTESIAN) {
                MathFunction.yScaleFactor = (double) ((JSpinner) e.getSource()).getValue();
                reDrawCurve();
                return;
            }
            if(MathFunction.coordinateSystem == MathFunction.POLAR) {
                MathFunction.rScaleFactor = (double) ((JSpinner) e.getSource()).getValue();
                reDrawCurve();
                return;
            }

        });

        angleLimitSpinner.addChangeListener(e -> {
                MathFunction.angleLimit = (double) ((JSpinner) e.getSource()).getValue();
                reDrawCurve();
                return;
        });

        currentFunction = new JLabel(MathFunction.currentFunctionText);
        currentFunction.setFont(currentFunction.getFont().deriveFont(16.0f));
        frame.add(currentFunction, BorderLayout.NORTH);

        graphPanel = new GraphPanel(graph);

        frame.add(graphPanel, BorderLayout.CENTER);
        frame.add(optionsToolbar, BorderLayout.SOUTH);

        frame.setVisible(true);

    }

    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("Change") && MathFunction.coordinateSystem == MathFunction.CARTESIAN) {
            MathFunction.functionNumber++;
            if(MathFunction.functionNumber > MathFunction.maxNumOfCartesianFunctions) MathFunction.functionNumber = 0;
            this.reDrawCurve();
            this.currentFunction.setText(MathFunction.currentFunctionText);
            return;
        }

        if(e.getActionCommand().equals("Change") && MathFunction.coordinateSystem == MathFunction.POLAR) {
            MathFunction.functionNumber++;
            if(MathFunction.functionNumber > MathFunction.maxNumOfPolarFunctions) MathFunction.functionNumber = 0;
            this.reDrawCurve();
            this.currentFunction.setText(MathFunction.currentFunctionText);
            return;
        }


        if (e.getActionCommand().equals("Animate") && animating == false) {
                this.animate();
                return;
        }

        if (e.getActionCommand().equals("System")) {
            if(MathFunction.coordinateSystem == MathFunction.POLAR) {
                MathFunction.coordinateSystem = MathFunction.CARTESIAN;
                xFactor.setText("x Factor: ");
                xScaleFactorSpinner.setValue(MathFunction.xScaleFactor);
                yFactor.setText("y Factor: ");
                yScaleFactorSpinner.setValue(MathFunction.yScaleFactor);
                angleLimit.setVisible(false);
                angleLimitSpinner.setVisible(false);
                MathFunction.functionNumber = 0;
                this.reDrawCurve();
                currentFunction.setText(MathFunction.currentFunctionText);
                return;
            }

            if(MathFunction.coordinateSystem == MathFunction.CARTESIAN) {
                MathFunction.coordinateSystem = MathFunction.POLAR;
                xFactor.setText("theta Factor: ");
                xScaleFactorSpinner.setValue(MathFunction.thetaScaleFactor);
                yFactor.setText("r Factor: ");
                yScaleFactorSpinner.setValue(MathFunction.rScaleFactor);
                angleLimit.setVisible(true);
                angleLimitSpinner.setVisible(true);
                MathFunction.functionNumber = 0;
                this.reDrawCurve();
                currentFunction.setText(MathFunction.currentFunctionText);
                return;
            }
        }

    }

    public void reDrawCurve() {
        BufferedImage tempImage = MathFunction.drawCurve(new BufferedImage(Window.WINDOW_WIDTH, Window.WINDOW_HEIGHT, BufferedImage.TYPE_INT_RGB));
        graphPanel.refreshGraph(tempImage);
        frame.repaint();
    }

    public void animate() {



        if(MathFunction.coordinateSystem == MathFunction.CARTESIAN) {
            Runnable animate = () -> {
                animating = true;
                double tempX = MathFunction.xScaleFactor;
                double tempY = MathFunction.yScaleFactor;
                for (double i = 0; i < 100; i++) {
                    double changeX = Math.sin((i / 50) * Math.PI) / 100;
                    double changeY = (Math.sin((i / 50) * (Math.PI * 2)) / 100);

                    MathFunction.xScaleFactor += changeX;
                    MathFunction.yScaleFactor += changeY;

                    this.reDrawCurve();
                    try {
                        TimeUnit.MILLISECONDS.sleep(10);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                    }
                }
                MathFunction.xScaleFactor = tempX;
                MathFunction.yScaleFactor = tempY;
                this.reDrawCurve();
                animating = false;
            };

            Thread run = new Thread(animate);
            run.start();
            return;
        }

        if(MathFunction.coordinateSystem == MathFunction.POLAR) {
            Runnable animate = () -> {
                animating = true;

                double changeThetaFactor;
                double tempThetaFactor = MathFunction.thetaScaleFactor;
                for (double i = 0; i < 100; i++) {

                    changeThetaFactor = Math.sin((i / 50) * Math.PI);
                    MathFunction.thetaScaleFactor -= changeThetaFactor / 10;

                    this.reDrawCurve();

                    try {
                        TimeUnit.MILLISECONDS.sleep(20);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                    }
                }
                MathFunction.thetaScaleFactor = tempThetaFactor;
                this.reDrawCurve();
                animating = false;
                return;
            };

            Thread run = new Thread(animate);
            run.start();
            return;
        }

        return;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(
                new Runnable(){
                    public void run(){
                        Window window1 = new Window();
                    }
                });
    }


}
