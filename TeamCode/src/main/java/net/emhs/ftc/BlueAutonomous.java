package net.emhs.ftc;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

@Autonomous
public class BlueAutonomous extends LinearOpMode {

    private DcMotor frontLeft, frontRight, backLeft, backRight;

    double multiplier = 0;

    @Override
    public void runOpMode() throws InterruptedException {

        waitForStart();

        frontRight = hardwareMap.get(DcMotor.class, "frontLeft");
        frontLeft = hardwareMap.get(DcMotor.class, "frontRight");
        backRight = hardwareMap.get(DcMotor.class, "backLeft");
        backLeft = hardwareMap.get(DcMotor.class, "backRight");

        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        resetRuntime();

        while (opModeIsActive()) {

            if (multiplier < 1) {
                multiplier = getRuntime();
            }

            //.88 front wheels = straight strafe

            if (getRuntime() < 3) {
                setWheelPower(.6*multiplier, -1*multiplier, .522*multiplier, -.87*multiplier);
            } else {
                stopWheels();
            }

            telemetry.addData("front right", frontRight.getCurrentPosition());
            telemetry.addData("front left", frontLeft.getCurrentPosition());
            telemetry.addData("back right", backRight.getCurrentPosition());
            telemetry.addData("back left", backLeft.getCurrentPosition());
            telemetry.update();
        }
    }

    public void setWheelPower (double fr, double fl, double br, double bl) {
        frontRight.setPower(fr);
        frontLeft.setPower(fl);
        backRight.setPower(br);
        backLeft.setPower(bl);
    }

    public void stopWheels() {
        frontRight.setPower(0);
        frontLeft.setPower(0);
        backRight.setPower(0);
        backLeft.setPower(0);
    }
}
