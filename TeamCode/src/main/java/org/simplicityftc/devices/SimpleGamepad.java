package org.simplicityftc.devices;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.simplicityftc.util.SimpleOpMode;

import java.nio.ByteBuffer;

public class SimpleGamepad extends Gamepad {
    private int lastButtons;
    private int currentButtons;

    public enum Button {
        touchpad_finger_1   (0x20000),
        touchpad_finger_2   (0x10000),
        touchpad            (0x08000),
        left_stick_button   (0x04000),
        right_stick_button  (0x02000),
        dpad_up             (0x01000),
        dpad_down           (0x00800),
        dpad_left           (0x00400),
        dpad_right          (0x00200),
        a                   (0x00100),
        cross               (0x00100),
        b                   (0x00080),
        circle              (0x00080),
        x                   (0x00040),
        square              (0x00040),
        y                   (0x00020),
        triangle            (0x00020),
        guide               (0x00010),
        ps                  (0x00010),
        start               (0x00008),
        options             (0x00008),
        back                (0x00004),
        share               (0x00004),
        left_bumper         (0x00002),
        right_bumper        (0x00001);

        public int integer;

        Button(int integer){
            this.integer = integer;
        }
    }

    public SimpleGamepad() {
        SimpleOpMode.deviceUpdateMethods.add(this::update);
    }

    public boolean wasJustPressed(Button button) {
        return (~lastButtons & currentButtons & button.integer) != 0;
    }

    private void update() {
        byte[] newData = toByteArray();
        ByteBuffer byteBuffer = getReadBuffer(newData);
        id = byteBuffer.getInt();
        timestamp = byteBuffer.getLong();
        left_stick_x = byteBuffer.getFloat();
        left_stick_y = byteBuffer.getFloat();
        right_stick_x = byteBuffer.getFloat();
        right_stick_y = byteBuffer.getFloat();
        left_trigger = byteBuffer.getFloat();
        right_trigger = byteBuffer.getFloat();

        lastButtons = currentButtons;
        currentButtons = byteBuffer.getInt();
    }
}
