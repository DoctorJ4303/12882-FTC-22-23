package net.emhs.ftc;

import com.qualcomm.hardware.motors.TetrixMotor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name="Main Tele-Op", group = "Main")
public class MainTeleOp extends LinearOpMode {

    private Servo claw1, claw2;
    private DcMotor frontLeft, frontRight, backLeft, backRight, rack1, rack2, arm;
    private boolean sprint, previousPadUp1, previousPadDown1, rightBumper1, leftBumper1, rightBumper2, leftBumper2, rightStick1, leftStick1, rightStick2, leftStick2;
    private boolean X1, Y1, A1, B1, X2, Y2, A2, B2, start1, back1, start2, back2, padUp1, padDown1;
    private double rightX1, rightY1, leftX1, leftY1, rightX2, rightY2, leftX2, leftY2, rightTrigger1, leftTrigger1, rightTrigger2, leftTrigger2;
    public double frontLeftPower, frontRightPower, backLeftPower, backRightPower, denominator;
    public double speed = 1;

    @Override
    public void runOpMode() throws InterruptedException {

        initializeMotors();

        waitForStart();

        while(opModeIsActive()) {
            //Updating
            updateInput();
            updateMovement();
            updateTelemetry();

            //Toggle of sprint
            speed += (padUp1 != previousPadUp1 && !padUp1 && speed < 1) ? 0.25 : (padDown1 != previousPadDown1 && !padDown1 && speed > 0.25) ? -0.25 : 0;
            previousPadDown1 = padDown1;
            previousPadUp1 = padUp1;

            //Claw movement
            claw1.setPosition((leftBumper2 || rightBumper2) ? 0.33 : 0);
            claw2.setPosition((leftBumper2 || rightBumper2) ? 0.67 : 1);

            //Rack movement
            if (rack1.getCurrentPosition() < 13650-(450*(leftY2)) && leftY2 < 0)
                rack1.setPower(-leftY2);
            else if (rack1.getCurrentPosition() > (550*(leftY2)) && leftY2 > 0)
                rack1.setPower(-leftY2);
            else
                rack1.setPower(0);

            if(rack2.getCurrentPosition() > -14000+(450*(rightY2)) && rightY2 < 0)
                rack2.setPower(rightY2);
            else if (rack2.getCurrentPosition() < -(550*(rightY2)) && rightY2 > 0)
                rack2.setPower(rightY2);
            else
                rack2.setPower(0);

            //Arm movement
            arm.setPower(rightTrigger2-leftTrigger2);
        }
    }

    public void updateMovement() {
        denominator = Math.max(Math.abs(leftY1) + Math.abs(leftX1), 1);
        frontLeftPower = -((leftY1 - leftX1 + rightX1) / denominator)*speed;
        backLeftPower = -((leftY1 + leftX1 + rightX1) / denominator)*speed;
        frontRightPower = ((leftY1 + leftX1 - rightX1) / denominator)*speed;
        backRightPower = ((leftY1 - leftX1 - rightX1) / denominator)*speed;

        frontLeft.setPower(frontLeftPower);
        backLeft.setPower(backLeftPower);
        frontRight.setPower(frontRightPower);
        backRight.setPower(backRightPower);
    }

    public void updateInput() {
        rightX1 = gamepad1.right_stick_x;
        rightY1 = gamepad1.right_stick_y;
        rightStick1 = gamepad1.right_stick_button;
        leftY1 = gamepad1.left_stick_y;
        leftX1 = gamepad1.left_stick_x;
        leftStick1 = gamepad1.left_stick_button;
        rightX2 = gamepad2.right_stick_x;
        rightY2 = gamepad2.right_stick_y;
        rightStick2 = gamepad2.right_stick_button;
        leftX2 = gamepad2.left_stick_x;
        leftY2 = gamepad2.left_stick_y;
        leftStick2 = gamepad2.left_stick_button;

        rightBumper1 = gamepad1.right_bumper;
        leftBumper1 = gamepad1.left_bumper;
        rightTrigger1 = gamepad1.right_trigger;
        leftTrigger1 = gamepad1.left_trigger;
        rightBumper2 = gamepad2.right_bumper;
        leftBumper2 = gamepad2.left_bumper;
        rightTrigger2 = gamepad2.right_trigger;
        leftTrigger2 = gamepad2.left_trigger;
        padUp1 = gamepad1.dpad_up;
        padDown1 = gamepad1.dpad_down;

        start1 = gamepad1.start;
        back1 = gamepad1.back;
        start2 = gamepad2.start;
        back2 = gamepad2.back;
        X1 = gamepad1.x;
        Y1 = gamepad1.y;
        A1 = gamepad1.a;
        B1 = gamepad1.b;
        X2 = gamepad2.x;
        Y2 = gamepad2.y;
        A2 = gamepad2.a;
        B2 = gamepad2.b;
    }

    public void updateTelemetry() {
        telemetry.addData("Rack 1 Position", rack1.getCurrentPosition());
        telemetry.addData("Rack 2 Position", rack2.getCurrentPosition());
        telemetry.addData("Arm Position", arm.getCurrentPosition());
        telemetry.update();
    }

    public void initializeMotors() {
        claw1 = hardwareMap.get(Servo.class, "claw1");
        claw2 = hardwareMap.get(Servo.class, "claw2");
        frontRight = hardwareMap.get(DcMotor.class, "frontLeft");
        frontLeft = hardwareMap.get(DcMotor.class, "frontRight");
        backRight = hardwareMap.get(DcMotor.class, "backLeft");
        backLeft = hardwareMap.get(DcMotor.class, "backRight");
        rack1 = hardwareMap.get(DcMotor.class, "rack1");
        rack2 = hardwareMap.get(DcMotor.class, "rack2");
        arm = hardwareMap.get(DcMotor.class, "arm");

        rack1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rack2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        arm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        claw1.setPosition(0.5);
        claw2.setPosition(0.5);
    }
}
