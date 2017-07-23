/**
 * Copyright © 2017 Sonicity (info@sonicity.nl)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package nl.sonicity.sha2017.cms.cmshabackend.titan.models;

/**
 * Created by hugo on 24/06/2017.
 */
public enum FixtureControlId {
    DIMMER(0x10), // The intensity control of a fixture.
    PAN(0x20), // The horizontal position control for a fixture.
    TILT(0x30), // The Vertical position control of a fixture.
    CYAN(0x40), // Cyan component used in subtracted colour mixing
    CYAN_ADDITIVE(0x41), // The amount of cyan in the output colour of a fixture.
    MAGENTA(0x50), // Magenta component used in subtracted colour mixing
    MAGENTA_ADDITIVE(0x51), // The amount of magenta in the output colour of a fixture.
    YELOW(0x60), // Yellow component used in subtracted colour mixing
    YELLOW_ADDITIVE(0x61), // The amount of yellow in the output colour of a fixture.
    RED(0xf0), // The amount of red in the output colour of a fixture.
    GREEN(0x100), // The amount of green in the output colour of a fixture.
    BLUE(0x110), // The amount of blue in the output colour of a fixture.
    AMBER(0x111), // The amount of Amber in the output colour of a fixture.
    WHITE(0x112), // The amount of White in the output colour of a fixture.
    WARM_WHITE(0x113), // The amount of Warm White in the output colour of a fixture.
    COLD_WHITE(0x114), // The amount of Cold White in the output colour of a fixture.
    CTO(0x115), // Colour temperature correction channel.
    UV(0x116), // UV Colour Channel
    LIME(0x117), // Lime Colour Channel
    COLOUR_1(0x70), // Colour wheel 1
    COLOUR_2(0x80), // Colour wheel 2
    GOBO_1(0x90), // Gobo wheel 1
    GOBO_2(0xa0), // Gobo wheel 2
    GOBO_1_ROTATE(0xb0), // Gobo wheel 1 rotation
    GOBO_2_ROTATE(0xc0), // Gobo wheel 2 rotation
    IRIS(0xd0), // Fixture iris
    FOCUS(0xe0), // Fixture focus
    ZOOM(0xe1), // Fixture Zoom
    KEYSTONE_TOP_LEFT_X(0x130), // Keystone top left x
    KEYSTONE_TOP_LEFT_Y(0x140), // Keystone top left Y
    KEYSTONE_TOP_RIGHT_X(0x150), // Keystone top right x
    KEYSTONE_TOP_RIGHT_Y(0x160), // Keystone top right y
    KEYSTONE_BOTTOM_RIGHT_X(0x170), // Keystone bottom right x
    KEYSTONE_BOTTOM_RIGHT_Y(0x180), // Keystone bottom right y
    KEYSTONE_BOTTOM_LEFT_X(0x190), // Keystone bottom left X
    KEYSTONE_BOTTOM_LEFT_Y(0x1a0), // Keystone bottom left Y
    KEYSTONE_1(0x2e0), // Position of the top keystone edge
    KEYSTONE_2(0x2f0), // Position of the right keystone edge
    KEYSTONE_3(0x300), // Position of the bottom keystone edge
    KEYSTONE_4(0x310), // Position of the left keystone edge
    KEYSTONE_1_ROTATION(0x320), // Rotation of the top keystone edge
    KEYSTONE_2_ROTATION(0x330), // Rotation of the right keystone edge
    KEYSTONE_3_ROTATION(0x340),
    KEYSTONE_4_ROTATION(0x350), // Rotation to the left keystone edge
    BLADE_TOP_LEFT(0x1b0), // Top blade left corner
    BLADE_TOP_RIGHT(0x1c0), // Top blade right corner
    BLADE_RIGHT_TOP(0x1d0), // Right blade top corner
    BLADE_RIGHT_BOTTOM(0x1e0), // Right blade bottom corner
    BLADE_BOTTOM_RIGHT(0x1f0), // Bottom blade right corner
    BLADE_BOTTOM_LEFT(0x200), // Bottom blade left corner
    BLADE_LEFT_BOTTOM(0x210), // Left blade bottom corner
    BLADE_LEFT_TOP(0x220), // Left blade top corner
    BLADE_1(0x230), // Blade 1 position
    BLADE_2(0x240), // Blade 2 position
    BLADE_3(0x250), // Blade 3 postion
    BLADE_4(0x260), // Blade 4 position
    BLADE_1_ROTATION(0x270), // Blade 1 Rotation
    BLADE_2_ROTATION(0x280), // Blade 2 Rotation
    BLADE_3_ROTATION(0x290), // Blade 3 Rotation
    BLADE_4_ROTATION(0x2a0), // Blade 4 Rotation
    BLADE_ALL_ROTATION(0x2b0), // Blade system overall rotate
    KEYSTONE_X_RATIO(0x2c0), // Keystone x ratio
    KEYSTONE_Y_RATIO(0x2d0), // Keystone y ratio
    IMAGE_ZOOM(0x380), // Image Size
    ASPECT_RATIO(0x381), // Aspect Ratio
    ASPECT_MODE(0x382), // Aspect Mode
    MTC_HOUR(0x383), // MTC Hour
    MTC_MINUTE(0x384), // MTC Minute
    MTC_SECOND(0x385), // MTC Second
    MTC_FRAME(0x386), // MTC Frame
    PLAY_MODE(0x387), // Play Mode
    PLAY_SPEED(0x388), // Play Speed
    IN_POINT(0x389), // In Point
    OUT_POINT(0x38a); // Out Point

    private int value;

    FixtureControlId(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
