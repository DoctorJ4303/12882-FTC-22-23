package net.emhs.ftc;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name="Calibration Tele-Op", group = "Main")
public class CalibrationTeleOp extends LinearOpMode {

    private Servo claw1, claw2;
    private DcMotor frontLeft, frontRight, backLeft, backRight, rack1, rack2, arm;
    private boolean sprint, rightBumper1, leftBumper1, rightBumper2, leftBumper2, rightStick1, leftStick1, rightStick2, leftStick2;
    private boolean X1, Y1, A1, B1, X2, Y2, A2, B2, start1, back1, start2, back2;
    private double rightX1, rightY1, leftX1, leftY1, rightX2, rightY2, leftX2, leftY2, rightTrigger1, leftTrigger1, rightTrigger2, leftTrigger2;
    public double frontLeftPower, frontRightPower, backLeftPower, backRightPower, denominator;
    private int count = 0;

    @Override
    public void runOpMode() throws InterruptedException {

        initializeMotors();

        waitForStart();

        while(opModeIsActive()) {
            //Updating
            updateInput();
            updateMovement();

            rack1.setPower(-leftY2);
            rack2.setPower(rightY2);
            arm.setPower(rightTrigger2-leftTrigger2);

            telemetry.addData("Rack 1 position: ", rack1.getCurrentPosition());
            telemetry.addData("Rack 2 position: ", rack2.getCurrentPosition());
            telemetry.addData("Arm Position", arm.getCurrentPosition());
            telemetry.update();

            if (back1 && back2) {
                exit();
                break;
            }

        }
    }

    public void updateMovement() {
        denominator = Math.max(Math.abs(leftY1) + Math.abs(leftX1), 1);
        frontLeftPower = -((leftY1 - leftX1 + rightX1) / denominator)/(sprint ? 1 : 2);
        backLeftPower = -((leftY1 + leftX1 + rightX1) / denominator)/(sprint ? 1 : 2);
        frontRightPower = ((leftY1 + leftX1 - rightX1) / denominator)/(sprint ? 1 : 2);
        backRightPower = ((leftY1 - leftX1 - rightX1) / denominator)/(sprint ? 1 : 2);

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

    public void exit() {
        rack1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rack2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }
}
